package plutarch.nlp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 6/24/2017.
 */
public class HistorySentence
{
    public String Sentence;
    public Topic Topic;
    public List<HistoryPhrase> Phrases = new ArrayList<HistoryPhrase>();

    public HistorySentence() {

    }

    public void AddPhrase(HistoryPhrase phrase, Topic topic) {
        phrase.Sentence = Sentence;

        if (phrase.People.size() == 0 && topic.getNer() == "PERSON") {
            phrase.People.add(topic.getWord());
        }

        if (phrase.Locations.size() == 0 && topic.getNer() == "LOCATION") {
            phrase.People.add(topic.getWord());
        }

        if (phrase.Organizations.size() == 0 && topic.getNer() == "ORGANIZATION") {
            phrase.People.add(topic.getWord());
        }

        Phrases.add(phrase);
    }
}
