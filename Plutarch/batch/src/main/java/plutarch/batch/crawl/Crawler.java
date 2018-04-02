package plutarch.batch.crawl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import plutarch.batch.service.TokenService;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 8/25/2017.
 */
public abstract class Crawler {
    public abstract String Crawl(Topic topic);
}
