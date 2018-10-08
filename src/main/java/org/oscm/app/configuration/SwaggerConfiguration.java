/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/
package org.oscm.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final Set<String> DEFAULT_PRODUCES = new HashSet<>(
            Arrays.asList("application/json"));
    public static final Set<String> DEFAULT_CONSUMES = new HashSet<>(
            Arrays.asList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(DEFAULT_PRODUCES).consumes(DEFAULT_CONSUMES)
                .enableUrlTemplating(true).useDefaultResponseMessages(false)
                .select().apis(RequestHandlerSelectors
                        .basePackage("org.oscm.app.resource"))
                .build();
    }

}
