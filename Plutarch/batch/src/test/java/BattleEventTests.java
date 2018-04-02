import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import plutarch.batch.service.TokenService;
import plutarch.nlp.helper.DateHelper;
import plutarch.nlp.model.HistoryEvent;
import plutarch.nlp.model.HistoryEventType;
import plutarch.nlp.model.Resource;
import plutarch.nlp.model.Topic;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joshs on 6/25/2017.
 */

@ContextConfiguration(classes = {TestConfig.class})
public class BattleEventTests extends TestConfig{

    @Autowired
    private TokenService tokenService;

    private Resource resource;

    public BattleEventTests() {
        resource = new Resource();
        resource.setUrl("test");
    }

    @PostConstruct
    private void initialize()
    {
        // Setup the token service with Battle events only
        List<HistoryEventType> historyEventTypes = new ArrayList<>();
        historyEventTypes.add(HistoryEventType.Battle);

        tokenService.Initialize(historyEventTypes);
    }

    @Test
    public void evaluateSimpleBattleEvent() {
        // TODO: implement for battle below
        Topic sentenceTopic = new Topic("Dwight D. Eisenhower", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "The Germans launched a surprise counter offensive, in the Battle of the Bulge in December 1944, which the Allies turned back in early 1945 after Eisenhower repositioned his armies and improved weather allowed the Air Force to engage");

        Assert.assertNotEquals(events.size(), 0);

        /*Assert.assertEquals(events.get(0).getHistoryEventType(), HistoryEventType.Birth);
        Assert.assertEquals(events.get(0).gethDate().getDate(), DateHelper.getInstance().Parse("565 BC").getDate());

        Assert.assertEquals(events.get(1).getHistoryEventType(), HistoryEventType.Death);
        Assert.assertEquals(events.get(1).gethDate().getDate(), DateHelper.getInstance().Parse("515 BC").getDate());*/
    }
}
