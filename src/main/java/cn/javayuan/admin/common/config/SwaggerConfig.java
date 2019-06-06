package cn.javayuan.admin.common.config;

import cn.javayuan.admin.common.authentication.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        ParameterBuilder paramBuilder = new ParameterBuilder();
        List<Parameter> params = new ArrayList<>();
        paramBuilder.name(JWTFilter.TOKEN).modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        params.add(paramBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(params)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .consumes(new HashSet<>(Collections.singletonList("application/json")))
                .produces(new HashSet<>(Collections.singletonList("application/json")));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Admin REST API",
                "Admin description of API.",
                "development",
                "Terms of service",
                new Contact("limy", "http://www.javayuan.cn", "limy@videon.cn"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
