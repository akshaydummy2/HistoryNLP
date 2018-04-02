import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by joshs on 1/27/2018.
 */
@Configuration
@TestPropertySource(locations="classpath:application.properties")
@ComponentScan(basePackages = {"plutarch.batch", "plutarch.nlp"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestConfig {

}
