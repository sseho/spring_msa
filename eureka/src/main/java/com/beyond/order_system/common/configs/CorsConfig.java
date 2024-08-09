package com.beyond.order_system.common.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
//
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**") // 접근할 수 있는 uri 허용 범위
                .allowedOrigins("http://localhost:8081") // 허용 url명시
                .allowedMethods("*") // 모든 메서드에 대해 허용
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
