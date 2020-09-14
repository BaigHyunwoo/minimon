package com.minimon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        int sortOrder = 0;
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .tags(
                        new Tag("Main Controller", "MAIN", sortOrder++),
                        new Tag("Url Controller", "URL", sortOrder++),
                        new Tag("Api Controller", "API", sortOrder++),
                        new Tag("Transaction Controller", "TRANSACTION", sortOrder++),
                        new Tag("Result Controller", "RESULT", sortOrder++)
                );
    }
}
