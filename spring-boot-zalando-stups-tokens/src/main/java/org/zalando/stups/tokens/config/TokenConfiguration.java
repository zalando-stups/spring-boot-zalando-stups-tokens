/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
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
package org.zalando.stups.tokens.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  jbellmann
 */
public class TokenConfiguration {

    /**
     * Identifier for this configuration.
     */
    private String tokenId;

    /**
     * Expose this config as prepared bean.
     */
    private boolean exposeAsBean = true;

    /**
     * Scopes to be requested for this service.
     */
    private List<String> scopes = new ArrayList<String>(0);

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(final String tokenId) {
        this.tokenId = tokenId;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(final List<String> scopes) {
        this.scopes = scopes;
    }

    public boolean isExposeAsBean() {
        return exposeAsBean;
    }

    public void setExposeAsBean(boolean exposeAsBean) {
        this.exposeAsBean = exposeAsBean;
    }

}
