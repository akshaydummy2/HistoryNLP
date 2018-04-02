package plutarch.batch.EventEvaluators;

import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryPhrase;
import plutarch.nlp.model.Resource;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Created by joshs on 10/14/2017.
 */
public interface EventEvaluator {
    List<HistoryEvent> Evaluate(Resource resource, HistoryPhrase phrase) throws ValidationException;
}
