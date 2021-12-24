package com.renyujie.server.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName SwaggerConfig.java
 * @Description Swagger配置类
 * @createTime 2021年12月23日 16:36:00
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.renyujie.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("yuneban接口文档")
                .description("yuneban接口文档s")
                .contact(new Contact("renyujie", "http://localhost:8081/doc.html", "renyujie@ryj.com"))
                .version("1.0")
                .build();
    }
    /**
     * @Description: 设置swagger需要登录认证的接口路径
     */
    private List<SecurityContext> securityContexts() {

        List<SecurityContext> list = new ArrayList<>();
        list.add(getContextByPath("/hello/.*"));
        return list;
    }
    /**
     * @Description: 给swagger配置规则，即SecurityContext）
     */
    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                //正则表达式格式
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }
    /**
     * @Description: securityReferences授权的规则
     */
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }
    /**
     * @Description: 设置swagger的securitySchemes
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeys;
    }






}
