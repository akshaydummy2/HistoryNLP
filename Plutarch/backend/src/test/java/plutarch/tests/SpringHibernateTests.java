package plutarch.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import plutarch.nlp.model.Resource;
import plutarch.nlp.service.DataService;

/**
 * Created by joshs on 1/27/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class SpringHibernateTests extends TestConfig {

    @Autowired
    private DataService dataService;

    @Test
    public void testNextResource() {
        Resource resource = dataService.GetNextResource();
        Assert.assertTrue(true);
    }
}
