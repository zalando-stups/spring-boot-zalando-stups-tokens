package org.zalando.stups.tokens;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.diagnostics.FailureAnalysis;

import com.unknown.pkg.TokenTestApplication;

public class NotExistentCredentialsDirectoryFailureAnalyzerTest {

    @Test
    public void contextLoads() {
        CredentialsDirectoryNotExistsFailureAnalyzer fa = new CredentialsDirectoryNotExistsFailureAnalyzer();
        FailureAnalysis analysis = fa.analyze(new RuntimeException(),
                new CredentialsDirectoryNotExistsException("/not/existent/path"));
        assertThat(analysis.getAction()).isEqualTo("Make sure 'CREDENTIALS_DIR' exists before application starts.");
        assertThat(analysis.getDescription())
                .isEqualTo("The configured 'CREDENTIALS_DIR' /not/existent/path does not exists.");
    }

    @Test(expected=BeanCreationException.class)
    public void testStartup() {
        SpringApplication.run(TokenTestApplication.class, "-Dspring.profiles.active=cd_not_exists");
    }

    @Test(expected=BeanCreationException.class)
    public void testStartupMocked() {
        SpringApplication.run(TokenTestApplication.class, "-Dspring.profiles.active=cd_not_exists_enableMock");
    }

}
