package plutarch.batch.EventEvaluators;

import plutarch.nlp.helper.DateHelper;
import plutarch.nlp.model.*;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 10/14/2017.
 */
public class BirthDeathEventEvaluator implements EventEvaluator {

    public List<HistoryEvent> Evaluate(Resource resource, HistoryPhrase phrase) throws ValidationException {
        List<HistoryEvent> events = new ArrayList<HistoryEvent>();

        String person = null;
        // Check for the correct name; if none return with no history events
        if (phrase.People.size() > 0) {
            person = phrase.People.get(0);
        } else {
            throw new ValidationException("No people found on phrase: " + phrase.Phrase);
        }

        // Ensure dates are unique
        List<HistoryDate> hDates = new ArrayList<HistoryDate>();
        for (String date : phrase.Dates) {
            HistoryDate hDate = DateHelper.getInstance().Parse(date);

            if (hDate != null && !hDates.contains(hDate)) {
                hDates.add(hDate);
            }
        }

        if (hDates.size() != 2) {
            throw new ValidationException("Expected to have 2 Dates but have: " + hDates.size());
        }

        // Create birth event
        HistoryEvent birth = new HistoryEvent();
        birth.AddPerson(person);
        birth.sethDate(hDates.get(0));
        birth.setHistoryEventType(HistoryEventType.Birth);
        birth.setPhrase(phrase.Phrase);
        birth.setResource(resource);

        events.add(birth);

        // Create death event
        HistoryEvent death = new HistoryEvent();
        death.AddPerson(person);
        death.sethDate(hDates.get(1));
        death.setHistoryEventType(HistoryEventType.Death);
        death.setPhrase(phrase.Phrase);
        death.setResource(resource);

        events.add(death);

        return events;
    }
}
