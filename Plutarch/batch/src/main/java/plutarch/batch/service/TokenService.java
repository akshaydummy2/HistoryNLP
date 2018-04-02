package plutarch.batch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.Env;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.NodePattern;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokensRegexAnnotator;
import edu.stanford.nlp.util.CoreMap;
import plutarch.BatchApplication;
import plutarch.batch.EventEvaluators.EventEvaluator;
import plutarch.batch.EventEvaluators.EventEvaluatorFactory;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.HistoryPhrase;
import plutarch.nlp.model.Topic;

// HELP: https://nlp.stanford.edu/software/tokensregex.html

/**
 * Created by joshs on 10/14/2017.
 */
@Service
public class TokenService {

    @Autowired
    private ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(BatchApplication.class);

    private static String RULES_FOLDER = "classpath:/Rules/";

    StanfordCoreNLP eventPipeline = null;
    CoreMapExpressionExtractor<MatchedExpression> eventExtractor = null;

    StanfordCoreNLP tokenPipeline = null;
    CoreMapExpressionExtractor<MatchedExpression> tokenExtractor = null;

    public TokenService() {

    }

    public void Initialize(List<HistoryEventType> historyEventTypes) {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

        resolveRulesFolder();

        setupEventPipeline(props, historyEventTypes);

        setupTokenPipeline(props);
    }

    private void setupEventPipeline(Properties props, List<HistoryEventType> historyEventTypes) {
        // Setup event pipeline
        eventPipeline = new StanfordCoreNLP(props);

        // Add in rule files
        List<String> eventRules = getEventRules(historyEventTypes);

        for (String ruleFile : eventRules) {
            eventPipeline.addAnnotator(new TokensRegexAnnotator(ruleFile));
        }

        // Create env as case insensitive
        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringMatchFlags(NodePattern.CASE_INSENSITIVE);
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);

        // Create extractor
        eventExtractor = CoreMapExpressionExtractor.createExtractorFromFiles(env, eventRules);
    }

    private List<String> getEventRules(List<HistoryEventType> historyEventTypes) {
        List<String> eventRules = new ArrayList<>();

        // Check history event types and add in rules accordingly
        if (historyEventTypes.contains(HistoryEventType.Birth) && historyEventTypes.contains(HistoryEventType.Death))
            eventRules.add(RULES_FOLDER + "Birth_Death.rule");

        if (historyEventTypes.contains(HistoryEventType.Birth))
            eventRules.add(RULES_FOLDER + "Birth.rule");

        if (historyEventTypes.contains(HistoryEventType.Death))
            eventRules.add(RULES_FOLDER + "Death.rule");

        if (historyEventTypes.contains(HistoryEventType.Battle))
            eventRules.add(RULES_FOLDER + "Battle.rule");

        return eventRules;
    }

    private void setupTokenPipeline(Properties props) {
        // Setup event pipeline
        tokenPipeline = new StanfordCoreNLP(props);

        // Add in rule files
        List<String> eventRules = new ArrayList<String>();
        eventRules.add(RULES_FOLDER + "Person.rule");
        eventRules.add(RULES_FOLDER + "Date.rule");
        eventRules.add(RULES_FOLDER + "Location.rule");
        eventRules.add(RULES_FOLDER + "Organization.rule");

        for(String ruleFile: eventRules) {
            tokenPipeline.addAnnotator(new TokensRegexAnnotator(ruleFile));
        }

        // Create env as case insensitive
        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringMatchFlags(NodePattern.CASE_INSENSITIVE);
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);

        // Create extractor
        tokenExtractor = CoreMapExpressionExtractor.createExtractorFromFiles(env, eventRules);
    }

    public List<HistoryEvent> EvaluateSentence(plutarch.nlp.model.Resource resource, String sentenceText) {
        // Check for he/she
        if (sentenceText.toLowerCase().startsWith("she") && resource.getTopic().getNer().equals("PERSON"))
            sentenceText = resource.getTopic().getWord() + sentenceText.substring(3, sentenceText.length());
        else if (sentenceText.toLowerCase().startsWith("he") && resource.getTopic().getNer().equals("PERSON"))
            sentenceText = resource.getTopic().getWord() + sentenceText.substring(2, sentenceText.length());

        // Run the evaluation
        List<HistoryEvent> hEvents = evaluateSentence(resource, sentenceText);

        // Reprocess with the Sentence topic involved if we didn't get any events
        if (hEvents.size() == 0 && !sentenceText.startsWith(resource.getTopic().getWord())) {
            sentenceText = resource.getTopic().getWord() + " - " + sentenceText;
            hEvents = EvaluateSentence(resource, sentenceText);
        }

        return hEvents;
    }

    private List<HistoryEvent> evaluateSentence(plutarch.nlp.model.Resource resource, String sentenceText) {
        List<HistoryEvent> hEvents = new ArrayList<HistoryEvent>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(sentenceText);

        // run all Annotators on this text
        eventPipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            List<MatchedExpression> matched = eventExtractor.extractExpressions(sentence);

            for(MatchedExpression phrase : matched) {
                try {
                    HistoryPhrase hPhrase = buildPhrase(phrase.getText());

                    EventEvaluator evaluator = EventEvaluatorFactory.Resolve((String) phrase.value.get());
                    if (evaluator != null) {
                        List<HistoryEvent> evalEvents = evaluator.Evaluate(resource, hPhrase);
                        hEvents.addAll(evalEvents);
                    }
                } catch (ValidationException validationEx) {
                    logger.debug(validationEx.toString());
                }
            }
        }

        return hEvents;
    }

    private HistoryPhrase buildPhrase(String phraseText) {
        HistoryPhrase hPhrase = new HistoryPhrase();
        hPhrase.Phrase = phraseText;

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(phraseText);

        // run all Annotators on this text
        tokenPipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            List<MatchedExpression> matched = tokenExtractor.extractExpressions(sentence);

            for(MatchedExpression phrase : matched) {
                String token = (String)phrase.value.get();

                switch (token) {
                    case "Person":
                        hPhrase.People.add(phrase.getText());
                        break;
                    case "Date":
                        hPhrase.Dates.add(phrase.getText());
                        break;
                    case "Location":
                        hPhrase.Locations.add(phrase.getText());
                        break;
                    case "Organization":
                        hPhrase.Organizations.add(phrase.getText());
                        break;
                }
            }
        }

        return hPhrase;
    }

    private void resolveRulesFolder() {
        Resource resource = resourceLoader.getResource(RULES_FOLDER);
        try {
            RULES_FOLDER = resource.getURI().getPath();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}
