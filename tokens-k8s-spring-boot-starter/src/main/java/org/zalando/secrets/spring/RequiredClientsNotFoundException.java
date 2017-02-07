package org.zalando.secrets.spring;

import java.util.List;

public class RequiredClientsNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final List<String> notFound;

    public RequiredClientsNotFoundException(List<String> notFound) {
        this.notFound = notFound;
    }

    public List<String> getRequiredClientsNotFound() {
        return notFound;
    }

    @Override
    public String getMessage() {
        return "Required clients not found.";
    }

}
