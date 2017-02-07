package org.zalando.secrets.spring.config;

import org.zalando.secrets.spring.SecretsProperties;

/**
 * 
 * @author jbellmann
 *
 */
public class CredentialsDirectoryNotExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final SecretsProperties secretsProperties;
    private static final String MESSAGE = "Credentials-Directory does not exists";

    public CredentialsDirectoryNotExistsException(SecretsProperties secretsProperties) {
        this.secretsProperties = secretsProperties;
    }

    public String getConfiguredCredentialsDirectory() {
        return secretsProperties.getCredentialsDirectory();
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
