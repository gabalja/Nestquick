package com.ssafy.back.config;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiResponses;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

//	Swagger-UI 2.x 확인
//	http://localhost:8080/{your-app-root}/swagger-ui.html
//	Swagger-UI 3.x 확인
//	http://localhost:8080/{your-app-root}/swagger-ui/index.html

	private String version = "V1";
	private String title = "Final nestquick API " + version;
	
	// 이거는 그냥 이런 형태로 쓴다고 생각하면 된다
	@Bean
	public Docket api() {
		// Docket으로 스웨거 문서를 만든다
		return new Docket(DocumentationType.SWAGGER_2).consumes(getConsumeContentTypes()).produces(getProduceContentTypes())
					.apiInfo(apiInfo()).groupName(version).select()	// groupName : 이 문서의 고유값? 고유 이름? 여러가지가 있는 경우 구분하기 위해서 
					.apis(RequestHandlerSelectors.basePackage("com.ssafy.back"))	// 내가 API로 만들고자하는 패키지 지정
					.paths(regex("/nestquick/.*")).build()	// 어떤 URL로 접근하는지
					.useDefaultResponseMessages(false);	// @ApiResponses가 관련된 설정
	}
	
	// 들어오는 컨텐츠 타입
	private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
//      consumes.add("application/xml;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

	// 나가는 컨텐츠 타입
    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    // http://localhost/swagger-ui/index.html를 켰을 때 상단에 나오는 설명 부분
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(title)
				.description("<h3>Final nestquick API</h3>손의성 이경배 이지원<br>")	//<img src=\"/assets/img/ssafy_logo.png\" width=\"150\"> 
				.contact(new Contact("SSAFY", "https://edu.ssafy.com", "ssafy@ssafy.com"))
				.license("SSAFY License")
				.licenseUrl("https://www.ssafy.com/ksp/jsp/swp/etc/swpPrivacy.jsp")
				.version("1.0").build();
	}
	
}