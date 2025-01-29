package com.deep.electronic.store.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;




@Configuration
public class SwaggerConfig {
	String schemeName ="bearerScheme";
	
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList(schemeName))
				.components(new Components().addSecuritySchemes(schemeName, new SecurityScheme()
				.name(schemeName)
				.type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer")
				)
				)
				.info(new Info().title("Electronics Store")
				.description("This is a Electronics Shop")
				.version("1.0V")
				.license(new License().name("Apache 2.0").url("https://www.learncodewithduregsh.com"))
				.contact(new Contact().name("Shuvradeep Datta").email("sddatta505@gmail.com").url("https://www.google.com")));
				
				
				
	}
	
	
	
	
	
//	@Bean
//	Docket docket() {
//		Docket docket = new Docket(DocumentationType.SWAGGER_2);
//		
//		docket.apiInfo(getApiInfo());
//		
//		
//		return docket;
//	}
//
//	private ApiInfo getApiInfo() {
//		
//		
//		ApiInfo apiInfo =new ApiInfo
//				("Electronics Store Project", "This is backend project created by LCWD", "1.0.0V", "https://www.learncodewithdurgesh.com", 
//						new Contact("Shuvradeep Datta", "https://www.google.com", "sddatta505@gmail.com"), "License of APIS", "https://www.learncodewithdurgesh.com",new ArrayList<>());
//		return apiInfo;
//	}

}
