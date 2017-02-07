/**
 * Copyright (C) 2017 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
