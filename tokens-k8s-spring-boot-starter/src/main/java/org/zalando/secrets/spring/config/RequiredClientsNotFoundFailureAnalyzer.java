package org.zalando.secrets.spring.config;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.zalando.secrets.ClientCredentials;
import org.zalando.secrets.spring.RequiredClientsNotFoundException;

/**
 * Give some hints what went wrong, which {@link ClientCredentials} couldn't be
 * resolved.
 * 
 * @author jbellmann
 *
 */
class RequiredClientsNotFoundFailureAnalyzer extends AbstractFailureAnalyzer<RequiredClientsNotFoundException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, RequiredClientsNotFoundException cause) {
        return new FailureAnalysis(
                "The following clients : '" + cause.getRequiredClientsNotFound().toString()
                        + "' are required but couldn't be resolved.",
                "Verify the configuration for 'tokens.required-clients' in your application.yaml file.\nDo you have to configure credentials somewhere else? (Kubernetes-Secrets, AWS-KMS, AWS-IAM). Have a look there too.",
                cause);
    }

}
