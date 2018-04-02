package plutarch.batch.EventEvaluators;

import plutarch.nlp.helper.DateHelper;
import plutarch.nlp.model.*;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshs on 10/14/2017.
 */
public class BattleEventEvaluator implements EventEvaluator {

    public List<HistoryEvent> Evaluate(Resource resource, HistoryPhrase phrase) throws ValidationException {
        List<HistoryEvent> events = new ArrayList<HistoryEvent>();

        // Ensure dates are unique
        List<HistoryDate> hDates = new ArrayList<HistoryDate>();
        for (String date : phrase.Dates) {
            HistoryDate hDate = DateHelper.getInstance().Parse(date);

            if (hDate != null && !hDates.contains(hDate)) {
                hDates.add(hDate);
            }
        }

        /*if (hDates.size() != 1) {
            throw new ValidationException("Expected to have 1 date but have: " + hDates.size());
        }*/

        // Create battle event
        HistoryEvent event = new HistoryEvent();
        event.sethDate(hDates.get(0));
        event.setHistoryEventType(HistoryEventType.Battle);
        event.setPhrase(phrase.Phrase);
        event.setResource(resource);

        // Add all People
        for (String person: phrase.People) {
            event.AddPerson(person);
        }

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
