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
        int index = 0;
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .tags(
                        new Tag("CommonConfig Controller", "설정 관리", index++),
                        new Tag("Monitoring Url Controller", "URL 모니터링 관리", index++),
                        new Tag("Monitoring Api Controller", "API 모니터링 관리", index++),
                        new Tag("Monitoring Act Controller", "ACT 모니터링 관리", index++),
                        new Tag("Result Controller", "RESULT", index++)
                );
    }
}
