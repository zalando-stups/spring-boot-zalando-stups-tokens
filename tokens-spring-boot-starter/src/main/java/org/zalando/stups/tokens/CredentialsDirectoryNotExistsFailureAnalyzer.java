package org.zalando.stups.tokens;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

class CredentialsDirectoryNotExistsFailureAnalyzer
        extends AbstractFailureAnalyzer<CredentialsDirectoryNotExistsException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, CredentialsDirectoryNotExistsException cause) {
        String message = String.format("The configured 'CREDENTIALS_DIR' %s does not exists.",
                cause.getCredentialsDirectory());
        String action = "Make sure 'CREDENTIALS_DIR' exists before application starts.";
        return new FailureAnalysis(message, action, cause);
    }

}
