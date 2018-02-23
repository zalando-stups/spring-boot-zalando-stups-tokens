package org.zalando.stups.tokens;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.unknown.pkg.TokenTestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TokenTestApplication.class }, webEnvironment = RANDOM_PORT)
@ActiveProfiles("cd_not_exists_static_tokens")
public class CdNotExistsStaticTokensTest {

    @Rule
    public final ProvideSystemProperty myPropertyHasMyValue = new ProvideSystemProperty("OAUTH2_ACCESS_TOKENS", "");

    @Test
    public void contextLoads() {

    }

}
