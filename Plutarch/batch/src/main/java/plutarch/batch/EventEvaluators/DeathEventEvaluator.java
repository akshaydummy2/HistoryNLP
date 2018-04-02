package plutarch.batch.EventEvaluators;

import plutarch.nlp.helper.DateHelper;
import plutarch.nlp.model.*;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 10/14/2017.
 */
public class DeathEventEvaluator implements EventEvaluator {

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

        if (hDates.size() != 1) {
            throw new ValidationException("Expected to have 1 date but have: " + hDates.size());
        }

        // Create birth event
        HistoryEvent event = new HistoryEvent();
        event.AddPerson(person);
        event.sethDate(hDates.get(0));
        event.setHistoryEventType(HistoryEventType.Death);
        event.setPhrase(phrase.Phrase);
        event.setResource(resource);

        // Add all Locations
        for (String location: phrase.Locations) {
            event.AddLocation(location);
        }

        // Add all organizations
        for (String org: phrase.Organizations) {
            // Add first organization to the event
            event.AddOrganization(org);
        }

        events.add(event);

        return events;
    }
}
