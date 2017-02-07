package org.zalando.secrets.spring.config;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * {@link FailureAnalyzer} that gives some hints what was going wrong.
 * 
 * @author jbellmann
 *
 */
class CredentialsDirectoryNotExistsFailureAnalyzer
        extends AbstractFailureAnalyzer<CredentialsDirectoryNotExistsException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, CredentialsDirectoryNotExistsException cause) {
        return new FailureAnalysis(
                "The configured credentials-directory '" + cause.getConfiguredCredentialsDirectory()
                        + "' does not exists.",
                "Verify the configuration for 'tokens.credentials-directory' in your application.yaml file", cause);
    }

}
