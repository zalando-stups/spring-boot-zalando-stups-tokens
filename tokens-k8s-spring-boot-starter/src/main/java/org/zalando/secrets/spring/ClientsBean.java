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
import org.zalando.secrets.ClientCredentials;
import org.zalando.secrets.Clients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientsBean implements Clients {

    private static final String CLIENT_SECRET_SUFFIX = "-client-secret";
    private static final String CLIENT_ID_SUFFIX = "-client-id";

    private final SecretsProperties secretsProperties;

    private final Map<String, ClientCredentials> clients = new HashMap<>();

    private final FilenameFilter clientSecretFilenameFilter = new EndsWithFilenameFilter(CLIENT_SECRET_SUFFIX);

    public ClientsBean(SecretsProperties secretsProperties) {
        this.secretsProperties = secretsProperties;
    }

    @Override
    public Optional<ClientCredentials> get(String identifier) {
        return Optional.ofNullable(getClientInternal(identifier));
    }

    private ClientCredentials getClientInternal(String identifier) {
        return clients.get(identifier);
    }

    @PostConstruct
    public void initialize() {
        readFromFilesystem();
        clients.keySet().stream().forEach(c -> {
            log.info("registered client : {}", c);
        });
        List<String> requiredClientsNotFound = secretsProperties.getRequiredClients()
                                                                .stream()
                                                                .filter((client) -> !clients.keySet().contains(client))
                                                                .collect(toList());
        if (requiredClientsNotFound.size() > 0) {
            throw new RequiredClientsNotFoundException(requiredClientsNotFound);
        }
    }

    @Scheduled(initialDelay = 5_000, fixedDelay = 5_000)
    public void readScheduled() {
        readFromFilesystem();
    }

    protected void readFromFilesystem() {
        final String credentialsDirectoryPath = requireNonNull(secretsProperties.getCredentialsDirectory());
        final File[] clientSecretFiles = new File(credentialsDirectoryPath).listFiles(clientSecretFilenameFilter);
        asList(clientSecretFiles)
                .stream()
                .map(f -> buildClient(f))
                .filter(Objects::nonNull)
                .collect(toList())
                .forEach(c -> {
                    clients.put(c.getName(), c);
                });
    }

    protected ClientDto buildClient(File clientSecretFile) {
        final String clientName = clientSecretFile.getName().replace(CLIENT_SECRET_SUFFIX, "");

        try {
            String clientSecret = readContent(clientSecretFile.getAbsolutePath());
            String clientId = readContent(clientIdFilePath(clientSecretFile.getParentFile(), clientName));
            return ClientDto.builder().name(clientName).id(clientId).secret(clientSecret).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String clientIdFilePath(File parentDirectory, String clientName) {
        return new File(parentDirectory, clientName + CLIENT_ID_SUFFIX).getAbsolutePath();
    }
}
