package org.zalando.stups.tokens;

class CredentialsDirectoryNotExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String credentialsDirectory;

    CredentialsDirectoryNotExistsException(String credentialsDirectory) {
        this.credentialsDirectory = credentialsDirectory;
    }

    public String getCredentialsDirectory() {
        return credentialsDirectory;
    }
}
