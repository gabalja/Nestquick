package com.ssafy.back.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.ssafy.back.jwt.interceptor.AuthInterceptor;

@Configuration
@EnableAspectJAutoProxy
@MapperScan(basePackages = { "com.ssafy.**.dao", "com.ssafy.**.mapper" })
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	private final String uploadFilePath;
	
	@Autowired
	private final AuthInterceptor authInterceptor;

	// 파일 업로드 설정
	// 경로 : 이전에는 내 프로젝트 내 경로였지만, application.properties에 설정해둔 변수를 통해 경로를 가져온다
	//JWT인증 인터셉터 설정
	public WebMvcConfiguration(@Value("${file.path.upload-files}") String uploadFilePath, AuthInterceptor authInterceptor) {
		this.uploadFilePath = uploadFilePath;
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:5173")
				.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
						HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
						HttpMethod.PATCH.name())
				.allowCredentials(true)	//쿠키 요청 허용
				.exposedHeaders("HttpHeaders.CONTENT_DISPOSITION")	//브라우저의 스크립트가 사용할 수 있는 응답 헤더 지정
				.maxAge(3600); // 3600초 동안 preflight 결과를 캐시에 저장
	}

	// assets라는 경로가 URL에 적히면 페이지로 해석하지 않겠다(?)
	// <resources>
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/file/**").addResourceLocations("file:///" + uploadFilePath + "/")
				.setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor);
	}
}