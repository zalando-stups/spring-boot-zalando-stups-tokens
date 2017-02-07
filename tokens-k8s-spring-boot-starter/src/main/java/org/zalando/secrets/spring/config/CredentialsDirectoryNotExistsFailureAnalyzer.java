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
