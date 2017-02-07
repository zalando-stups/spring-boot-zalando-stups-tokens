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
