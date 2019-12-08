package com.conichi.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @Author Fazel Farnia
 * Swagger configuration class make a good documentation for clients who want to consume Apis
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo("Conichi Test Challenge", "Create microservice with spring boot and provide 3 Rest services.", "1.0", "", new Contact("Fazel Farnia", "http://twitter.com/masih0111", "fazel.farnia@gmail.com"), "", "", Collections.emptyList()))
                .groupName("ConichiController")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .paths(PathSelectors.any())
                .build();
    }
}
