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
public abstract class Scraper {
    public abstract List<HistoryEvent> Scrape(TokenService tokenService, Resource resource, Document doc, List<HistoryEventType> historyEventTypes);

    // Gather all events from <P> tags
    protected List<HistoryEvent> scrapeParagraphs(TokenService tokenService, Resource resource, Document document) {
        List<HistoryEvent> events = new ArrayList<HistoryEvent>();

        Elements paragraphs = document.select("p");
        for (Element paragraph: paragraphs) {
            List<HistoryEvent> paragraphEvents = tokenService.EvaluateSentence(resource, paragraph.text());

            events.addAll(paragraphEvents);
        }

        return events;
    }
}
