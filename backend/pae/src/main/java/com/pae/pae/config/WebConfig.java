package com.pae.pae.config;

import com.pae.pae.middelware.JwtInterceptor; // Importación de JwtInterceptor
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor);
                //.addPathPatterns("/**") // Protege todos los endpoints
                //.excludePathPatterns("/usuaris/login", "/usuaris/register", "/usuaris"); // Excluye login y registro
    }
}
