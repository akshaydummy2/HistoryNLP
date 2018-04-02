package plutarch.nlp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 6/24/2017.
 */
public class HistoryPhrase
{
    public String Sentence;
    public String Phrase;
    public List<String> People = new ArrayList<String>();
    public List<String> Locations = new ArrayList<String>();
    public List<String> Organizations = new ArrayList<String>();
    public List<String> Dates = new ArrayList<String>();
    public List<String> Times = new ArrayList<String>();
    public List<String> Actions = new ArrayList<String>();

    public Topic Topic = null;

    public HistoryPhrase() {

    }
}
