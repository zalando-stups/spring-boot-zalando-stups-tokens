package issues;

import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.stups.tokens.AccessTokens;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IssuesApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("variable")
public class IssuesApplicationVariableTest {

    @BeforeClass
    public static void setUp() {
        System.setProperty("OAUTH2_ACCESS_TOKENS", "uid=98765,service-a=12345");
    }

    @AfterClass
    public static void tearDown() {
        System.getProperties().remove("OAUTH2_ACCESS_TOKENS");
        Assertions.assertThat(System.getProperty("OAUTH2_ACCESS_TOKENS")).isNull();
    }

    @Autowired
    private AccessTokens accessTokens;

    @Test
    public void startUp() {
        Assertions.assertThat(accessTokens).isNotNull();
        String token = accessTokens.get("uid");
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isNotEmpty();

        String serviceA = accessTokens.get("service-a");
        Assertions.assertThat(serviceA).isNotNull();
        Assertions.assertThat(serviceA).isNotEmpty();
    }

}
