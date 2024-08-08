package com.example.BeFETest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // 허용할 클라이언트 도메인
//                .allowedOrigins("http://43.200.199.72:5173") // 허용할 클라이언트 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                //.allowedHeaders("Content-Type")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

/*
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }


}

 */
