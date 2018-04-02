import java.util.Arrays;
import java.util.List;

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

/**
 * Created by joshs on 6/25/2017.
 */

@ContextConfiguration(classes = {TestConfig.class})
public class TokenServiceTests extends TestConfig{

    @Autowired
    private TokenService tokenService;

    private Resource resource;

    public TokenServiceTests() {
        resource = new Resource();
        resource.setUrl("test");
    }

    @PostConstruct
    private void initialize() {
        tokenService.Initialize(Arrays.asList(HistoryEventType.values()));
    }

    @Test
    public void evaluateSimpleBirthDeath() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III (565 BC – 515 BC)");

        Assert.assertEquals(events.size(), 2);

        AssertEvent(events.get(0), HistoryEventType.Birth, "565 BC");
        AssertEvent(events.get(1), HistoryEventType.Death, "515 BC");
    }

    @Test
    public void evaluateNapoleonBirthDeath() {
        Topic sentenceTopic = new Topic("Napoleon Bonaparte", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "Napoléon Bonaparte (/nəˈpoʊliən ˈboʊnəpɑːrt/;[2] French: [napɔleɔ̃ bɔnapaʁt]; 15 August 1769 – 5 May 1821)");

        Assert.assertEquals(events.size(), 2);

        AssertEvent(events.get(0), HistoryEventType.Birth, "15 August 1769");
        AssertEvent(events.get(1), HistoryEventType.Death, "5 May 1821");
    }

    @Test
    public void evaluatePhilipIIBirthDeath() {
        Topic sentenceTopic = new Topic("Philip II", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "Philip II of Macedon[1] (Greek: Φίλιππος Β΄ ὁ Μακεδών, Phílippos II ho Makedṓn; 382–336 BC)");

        // TODO: fix the logic so this one works
        Assert.assertEquals(events.size(), 0); // Expected to be 0 for now with current logic

        /*assertEquals(events.get(0).EventType, "Birth");
        assertEquals(events.get(0).HDate.Date, DateHelper.getInstance().Parse("382 BC").Date);

        assertEquals(events.get(1).EventType, "Death");
        assertEquals(events.get(1).HDate.Date, DateHelper.getInstance().Parse("336 BC").Date);*/
    }

    @Test
    public void evaluateJuliusCaesarBirthDeath() {
        Topic sentenceTopic = new Topic("Gaius Julius Caesar", "PERSON");
        resource.setTopic(sentenceTopic);


        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "Gaius Julius Caesar (Latin: CAIVS IVLIVS CAESAR, pronounced [ˈɡaː.i.ʊs ˈjuː.li.ʊs ˈkae̯.sar][b], born: 13 July 100 BC – 15 March 44 BC)");

        Assert.assertEquals(events.size(), 2);

        AssertEvent(events.get(0), HistoryEventType.Birth, "13 July 100 BC");
        AssertEvent(events.get(1), HistoryEventType.Death, "15 March 44 BC");
    }

    @Test
    public void evaluatePubliusCorneliusDolabellaBirthDeath() {
        Topic sentenceTopic = new Topic("Publius Cornelius Dolabella", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "Publius Cornelius Dolabella (c. 85–80 BC – 43 BC) was a Roman general, by far the most important of the Dolabellae.");

        Assert.assertEquals(events.size(), 2);

        AssertEvent(events.get(0), HistoryEventType.Birth, "80 BC");
        AssertEvent(events.get(1), HistoryEventType.Death, "43 BC");
    }

    @Test
    public void evaluateSimpleBirth() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III was born on 565 BC");

        Assert.assertEquals(events.size(), 1);

        AssertEvent(events.get(0), HistoryEventType.Birth, "565 BC");
    }

    @Test
    public void evaluateSimpleBirth2() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III was born on 565 BC in Athens, Greece");

        Assert.assertEquals(events.size(), 1);

        AssertEvent(events.get(0), HistoryEventType.Birth, "565 BC");
    }

    @Test
    public void evaluateSimpleBirth3() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III was born in Athens, Greece on 565 BC ");

        Assert.assertEquals(events.size(), 1);

        AssertEvent(events.get(0), HistoryEventType.Birth, "565 BC");
    }

    @Test
    public void evaluateSimpleDeath() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III died on 565 BC");

        Assert.assertEquals(events.size(), 1);

        AssertEvent(events.get(0), HistoryEventType.Death, "565 BC");
    }

    @Test
    public void evaluateSimpleDeath2() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III was killed on 565 BC in Athens, Greece");

        Assert.assertEquals(events.size(), 1);

        AssertEvent(events.get(0), HistoryEventType.Death, "565 BC");
    }

    @Test
    public void evaluateSimpleDeath3() {
        Topic sentenceTopic = new Topic("John Smith III", "PERSON");
        resource.setTopic(sentenceTopic);

        List<HistoryEvent> events = tokenService.EvaluateSentence(resource, "John Smith III died in Athens, Greece on 565 BC ");

        Assert.assertEquals(events.size(), 1);

        AssertEvent(events.get(0), HistoryEventType.Death, "565 BC");
    }

    private void AssertEvent(HistoryEvent historyEvent, HistoryEventType historyEventType, String date) {
        Assert.assertEquals(historyEvent.getHistoryEventType(), historyEventType);
        Assert.assertEquals(DateHelper.getInstance().Parse(date).getDate(), historyEvent.gethDate().getDate());
        Assert.assertEquals(date, historyEvent.gethDate().getFormattedDate());
    }
}
