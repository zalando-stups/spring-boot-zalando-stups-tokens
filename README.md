[![Build Status](https://travis-ci.org/zalando-stups/spring-boot-zalando-stups-tokens.svg?branch=master)](https://travis-ci.org/zalando-stups/spring-boot-zalando-stups-tokens) 
![Maven Central](https://img.shields.io/maven-central/v/org.zalando.stups/tokens-spring-boot-starter.svg)
[![codecov.io](https://codecov.io/github/zalando-stups/spring-boot-zalando-stups-tokens/coverage.svg?branch=master)](https://codecov.io/github/zalando-stups/spring-boot-zalando-stups-tokens?branch=master)


[![Try Initializer](https://img.shields.io/badge/assemble%20your%20spring--boot--app%20with-Zalando%20OSS%20Initializr-orange.svg)](https://initializr.machinery.zalan.do)

## Spring-Boot STUPS AccessTokens Support

Is a small wrapper around [Tokens](https://github.com/zalando-stups/tokens) with lifecycle-management and autoconfiguration-support in Spring-Boot applications.

###Build

    mvn install


###Install

Add the following to your pom.xml when using maven.

    <dependency>
        <groupId>org.zalando.stups</groupId>
        <artifactId>tokens-spring-boot-starter</artifactId>
        <version>${version}</version>
    </dependency>

###Configuration

    tokens:
        accessTokenUri: http://localhost:9191/access_token?realm=whatever
        credentialsDirectory: ${user.dir}/somepath/credentials
    
        token-configuration-list:
            - tokenId: firstService
              scopes:
                  - refole:read
                  - refole:write
                  - refole:all
            - tokenId: secondService
              scopes: singleScope:all

###Usage

With this in place you can use the 'AccessTokens' anywhere in your application (@Autowire directly or in a configuration class), use it directly or inject it into some 'TokenProvider'-implementations that delegate somehow.


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
