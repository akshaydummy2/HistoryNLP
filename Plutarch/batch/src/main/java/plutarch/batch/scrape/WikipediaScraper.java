package plutarch.batch.scrape;

import plutarch.nlp.model.HistoryEvent;
import plutarch.batch.service.TokenService;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 8/25/2017.
 */
public class WikipediaScraper extends Scraper {

    public List<HistoryEvent> Scrape(TokenService tokenService, Resource resource, Document doc, List<HistoryEventType> historyEventTypes) {
        List<HistoryEvent> events = new ArrayList<HistoryEvent>();

        // Scrape paragraphs
        events.addAll(this.scrapeParagraphs(tokenService, resource, doc));

        // Scrape side bar
        events.addAll(scrapeSideBar(tokenService, resource, doc));

        return events;
    }

    private List<HistoryEvent> scrapeSideBar(TokenService tokenService, Resource resource, Document doc) {
        // Process the wiki page
        Elements trArray = doc.select("table.infobox tr");

        // Create a string sentence from each table row
        List<String> sentences = new ArrayList<String>();
        for (Element tr: trArray) {
            String sentence = resource.getTopic().getWord() + " be ";

            // Find the header of the row
            sentence += tr.select("th").text() + " ";

            // Find the trunk elements of all td children and get their value
            Elements tdArray = tr.select("td");
            for (Element td: tdArray) {
                if (td.hasText()) {
                    sentence += td.text() + " ";
                }

                List<Element> trunkElements = getTrunkElements(td.children());
                for (Element trunkElement: trunkElements) {
                    if (td.hasText()) {
                        sentence += trunkElement.text() + " ";
                    }
                }
            }

            // Cleanse the sentence and add it to the list
            sentences.add(sentenceCleanser(sentence));
        }

        List<HistoryEvent> events = new ArrayList<HistoryEvent>();

        for (String sentence: sentences) {
            events.addAll(tokenService.EvaluateSentence(resource, sentence));
        }

        return events;
    }

    private List<Element> getTrunkElements(Elements elementArray) {
        List<Element> elements = new ArrayList<Element>();

        for (Element ele: elementArray) {
            if (ele.children().size() == 0) {
                elements.add(ele);
            } else {
                // Recursively look through the html tree for the root elements
                elements.addAll(getTrunkElements(ele.children()));
            }
        }

        return elements;
    }

    private String sentenceCleanser(String sentence) {
        sentence = sentence.replace("(", " ");
        sentence = sentence.replace(")", " ");
        sentence = sentence.replace(",", " ");

        sentence = sentence.replace(" BC ", " BC - ");

        return sentence;
    }
}
