package org.zalando.stups.tokens;

/**
 * @author  jbellmann
 */
class FixedTokenAccessTokenRefresher extends AccessTokenRefresher {

    private final String fixedToken;

    public FixedTokenAccessTokenRefresher(final AccessTokensBuilder configuration, final String fixedToken) {
        super(configuration);
        this.fixedToken = fixedToken;
    }

    @Override
    protected String getFixedToken() {
        return fixedToken;
    }

}
