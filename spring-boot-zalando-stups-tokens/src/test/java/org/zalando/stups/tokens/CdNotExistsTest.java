package org.zalando.stups.tokens;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.unknown.pkg.TokenTestApplication;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TokenTestApplication.class }, webEnvironment = RANDOM_PORT)
@ActiveProfiles("cd_not_exists")
public class CdNotExistsTest {

    @Test
    public void contextLoads() {

    }

}
