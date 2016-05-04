package org.zalando.stups.tokens.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Qualifier
@Documented
public @interface OAuth2RestOperations {

    /**
     * The 'tokenId'/'serviceId' the {@link RestOperations} instance is
     * configured for.
     * 
     * @see RestOperations
     * @see RestTemplate
     * @return
     */
    @AliasFor(annotation = Qualifier.class, attribute = "value")
    public String value();

}
