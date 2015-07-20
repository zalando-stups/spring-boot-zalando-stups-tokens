package org.zalando.stups.tokens;

import java.net.URI;

/**
 * @author  jbellmann
 */
class FixedTokenAccessTokenBuilder extends AccessTokensBuilder {

    private final String fixedToken;

    FixedTokenAccessTokenBuilder(final URI accessTokenUri, final String fixedToken) {
        super(accessTokenUri);
        this.fixedToken = fixedToken;
    }

    @Override
    protected AccessTokenRefresher getAccessTokenRefresher() {
        return new FixedTokenAccessTokenRefresher(this, fixedToken);
    }

}
