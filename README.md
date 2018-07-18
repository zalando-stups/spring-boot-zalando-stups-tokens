[![Build Status](https://travis-ci.org/zalando-stups/spring-boot-zalando-stups-tokens.svg?branch=master)](https://travis-ci.org/zalando-stups/spring-boot-zalando-stups-tokens) 
![Maven Central](https://img.shields.io/maven-central/v/org.zalando.stups/tokens-spring-boot-starter.svg)
[![codecov.io](https://codecov.io/github/zalando-stups/spring-boot-zalando-stups-tokens/coverage.svg?branch=master)](https://codecov.io/github/zalando-stups/spring-boot-zalando-stups-tokens?branch=master)


[![Try Initializer](https://img.shields.io/badge/assemble%20your%20spring--boot--app%20with-Zalando%20OSS%20Initializr-orange.svg)](https://initializr.machinery.zalan.do)

## Tokens Support for Spring-Boot apps

Is a small wrapper around [Tokens](https://github.com/zalando-stups/tokens) with lifecycle-management and autoconfiguration-support in Spring-Boot applications.

With this in place you can use the 'AccessTokens' anywhere in your application (@Autowire directly or in a configuration class), use it directly or inject it into some 'TokenProvider'-implementations that delegate somehow.

### Install

#### Maven

Add the following to your `pom.xml`:

```
    <dependency>
        <groupId>org.zalando.stups</groupId>
        <artifactId>tokens-spring-boot-starter</artifactId>
        <version>${version}</version>
    </dependency>
```

#### Gradle

Add the following to your `build.gradle`:

```
compile('org.zalando.stups:tokens:${version}')
```

### Usage in Zalandos K8s environment (with `PlatformCredentialsSet`)

No need to configure anything. Only put the dependency into your pom.xml.
It uses `/meta/credentials` as a default folder to look for provided tokens by `PlatformCredentialsSet`.

Want to migrate from STUPS to K8s? [See the hints](#migration-from-zalandos-stups-env-to-zalandos-k8s-env).

### Usage in Zalandos STUPS environment

```
    tokens:
        accessTokenUri: http://localhost:9191/access_token?realm=whatever
    
        token-configuration-list:
            - tokenId: firstService
              scopes:
                  - read
                  - write
                  - all
            - tokenId: secondService
              scopes: all
```

### Migration from Zalandos STUPS env to Zalandos K8s env

Most common issue users do is not mounting the the credentials. Make sure you have something like this
in your `deployment.yaml`:

```
...
          volumeMounts:
          - name: "{{ APPLICATION }}-credentials"
            mountPath: /meta/credentials
            readOnly: true
      volumes:
        - name: "{{ APPLICATION }}-credentials"
          secret:
            secretName: "{{ APPLICATION }}-credentials"
```

Another issue would be that identifiers/names for tokens have to be same as before in your `credentials.yaml`:

```
apiVersion: "zalando.org/v1"
kind: PlatformCredentialsSet
metadata:
   name: "{{ APPLICATION }}-credentials"
spec:
   application: "{{ APPLICATION }}"
   tokens:
     firstService:
       privileges:
         - com.zalando::read
         - com.zalando::write
         - com.zalando::all
     secondService:
       privileges:
         - com.zalando::all
```

### Build

```
./mvnw install
```

## License

Copyright © 2015 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
