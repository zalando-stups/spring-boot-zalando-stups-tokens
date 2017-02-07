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
package org.zalando.secrets.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("tokens")
public class SecretsProperties {

    private static final String DEFAULT_CREDENTIALS_DIRECTORY = "/meta/credentials";

    /**
     * Directory where credentials/token files will be mounted.
     */
    private String credentialsDirectory = DEFAULT_CREDENTIALS_DIRECTORY;

    /**
     * Indicates that listed 'clients' are mandatory.
     */
    private List<String> requiredClients = new ArrayList<>(0);

    /**
     * Indicates that listed 'tokens' are mandatory.
     */
    private List<String> requiredTokens = new ArrayList<>(0);

    /**
     * Enable/Disable configuration. Default is 'enabled=true'.
     */
    private boolean enabled = true;

}
