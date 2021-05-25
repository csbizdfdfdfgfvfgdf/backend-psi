package com.notepad.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static java.util.Collections.singletonList;

/**
 * The SwaggerConfig class sets controllers and models info
 * for showing in swagger-ui.
 *
 * @author  Zohaib Ali
 * @version 1.0
 * @since   2021-04-22
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Value("${swagger.enable}")
    private Boolean swaggerEnable;

    /**
     * Create a Docket bean that is used by swagger
     * to set configurations for swagger
     *
     */
    @Bean
    public Docket api() {
        //Adding uuid authentication header
//        ParameterBuilder aParameterBuilder = new ParameterBuilder();
//        aParameterBuilder.name("uuid").description("Id retreived by calling /auth/makeUuid").modelRef(
//        		new ModelRef("string")).parameterType("header").required(true).build();
//        List<Parameter> aParameters = new ArrayList<Parameter>();
//        aParameters.add(aParameterBuilder.build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//        	.select()
//            .apis(RequestHandlerSelectors.basePackage("com.notepad.controller"))
//            .paths(PathSelectors.any())
//            .build()
//            .apiInfo(apiInfo())
//            .globalOperationParameters(aParameters);


        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .select().apis(RequestHandlerSelectors.basePackage("com.notepad.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(singletonList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)
                                .hidden(true)
                                .defaultValue("Bearer " + "jwt-token")
                                .build()
                ))
                .apiInfo(apiInfo());
    }

    /**
     * Returns complete app Rest Api info
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        log.info("setting up info about API ");
        ApiInfo apiInfo = new ApiInfo("REST API's", "Network Notepad API documentation", "1.0", null, null, null, null, Collections.emptyList());
        return apiInfo;
    }
}
