package org.zalando.secrets.spring;

import java.util.List;

public class RequiredTokensNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final List<String> notFound;

    public RequiredTokensNotFoundException(List<String> notFound) {
        this.notFound = notFound;
    }

    public List<String> getRequiredTokensNotFound() {
        return notFound;
    }
}
