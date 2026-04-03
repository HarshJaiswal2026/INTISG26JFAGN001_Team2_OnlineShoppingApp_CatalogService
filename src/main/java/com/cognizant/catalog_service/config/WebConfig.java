package com.cognizant.catalog_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps the URL path to the physical folder
        registry.addResourceHandler("/api/v1/catalog/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}