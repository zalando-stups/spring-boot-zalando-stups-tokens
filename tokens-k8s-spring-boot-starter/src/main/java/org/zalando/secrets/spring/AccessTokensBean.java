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

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.zalando.secrets.spring.FileUtils.readContent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Scheduled;
import org.zalando.secrets.Authorization;
import org.zalando.secrets.Authorizations;
import org.zalando.stups.tokens.AccessToken;
import org.zalando.stups.tokens.AccessTokenUnavailableException;
import org.zalando.stups.tokens.AccessTokens;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessTokensBean implements AccessTokens, Authorizations {

    private static final String TOKEN_SECRET_SUFFIX = "-token-secret";
    private static final String TOKEN_TYPE_SUFFIX = "-token-type";
    private final SecretsProperties secretsProperties;

    private final Map<String, AccessTokenDto> accessTokens = new HashMap<>();

    private final FilenameFilter tokenSecretFilenameFilter = new EndsWithFilenameFilter(TOKEN_SECRET_SUFFIX);

    public AccessTokensBean(SecretsProperties secretsProperties) {
        this.secretsProperties = secretsProperties;
    }

    @Override
    public String get(Object identifier) throws AccessTokenUnavailableException {
        return getAccessToken(identifier).getToken();
    }

    @Override
    public AccessToken getAccessToken(Object identifier) throws AccessTokenUnavailableException {
        return getAccessTokenInternal(identifier);
    }

    protected AccessTokenDto getAccessTokenInternal(Object identifier) {
        return accessTokens.get(identifier);
    }

    @Override
    public void invalidate(Object arg0) {
        log.warn("Invalidate not supported!");
    }

    @Override
    public void stop() {
        log.warn("Stop method not supported!");
    }

    @Override
    public Authorization get(String identifier) {
        return getAccessTokenInternal(identifier);
    }

    @Override
    public Optional<Authorization> getOptional(String identifier) {
        return Optional.ofNullable(get(identifier));
    }

    @PostConstruct
    public void initialize() {
        readFromFilesystem();
        accessTokens.keySet().stream().forEach(at -> {
            log.info("registered token : {}", at);
        });
        List<String> requiredTokensNotFound = secretsProperties.getRequiredTokens()
                                                                .stream()
                                                                .filter((token) -> !accessTokens.keySet().contains(token))
                                                                .collect(toList());
        if (requiredTokensNotFound.size() > 0) {
            throw new RequiredTokensNotFoundException(requiredTokensNotFound);
        }
    }

    @Scheduled(initialDelay = 5_000, fixedDelay = 5_000)
    public void readScheduled() {
        readFromFilesystem();
    }

    protected void readFromFilesystem() {
        final String credentialsDirectoryPath = requireNonNull(secretsProperties.getCredentialsDirectory());
        final File[] tokenSecretFiles = new File(credentialsDirectoryPath).listFiles(tokenSecretFilenameFilter);
        asList(tokenSecretFiles)
                .stream()
                .map(f -> buildAccessTokenDto(f))
                .filter(Objects::nonNull)
                .collect(toList())
                .forEach(token -> {
                    accessTokens.put(token.getIdentfier(),token);
                });
    }

    protected AccessTokenDto buildAccessTokenDto(File tokenSecretFile) {
        final String clientName = tokenSecretFile.getName().replace(TOKEN_SECRET_SUFFIX, "");

        try {
            String secret = readContent(tokenSecretFile.getAbsolutePath());
            String type = readContent(tokenTypeFilePath(tokenSecretFile.getParentFile(), clientName));
            return AccessTokenDto.builder().identfier(clientName).type(type).secret(secret).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String tokenTypeFilePath(File parentDirectory, String clientName) {
        return new File(parentDirectory, clientName + TOKEN_TYPE_SUFFIX).getAbsolutePath();
    }
}
