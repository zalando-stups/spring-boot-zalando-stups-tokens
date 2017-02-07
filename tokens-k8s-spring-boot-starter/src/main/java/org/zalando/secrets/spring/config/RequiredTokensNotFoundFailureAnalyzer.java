package org.zalando.secrets.spring.config;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.zalando.secrets.spring.RequiredTokensNotFoundException;
import org.zalando.stups.tokens.AccessToken;

/**
 * Give some hints what went wrong, which {@link AccessToken} couldn't be
 * resolved.
 * 
 * @author jbellmann
 *
 */
class RequiredTokensNotFoundFailureAnalyzer extends AbstractFailureAnalyzer<RequiredTokensNotFoundException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, RequiredTokensNotFoundException cause) {
        return new FailureAnalysis(
                "The following tokens : '" + cause.getRequiredTokensNotFound().toString()
                        + "' are required but couldn't be resolved.",
                "Verify the configuration for 'tokens.required-tokens' in your application.yaml file.\nDo you have to configure credentials/tokens somewhere else? (Kubernetes-Secrets, AWS-KMS). Have a look there too.",
                cause);
    }

}
