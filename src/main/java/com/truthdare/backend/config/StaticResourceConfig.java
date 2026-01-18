package com.truthdare.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * Configuration to serve React static files
 * Serves the React build from the static directory
 * Only handles non-API routes to avoid interfering with REST endpoints
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static files from React build
        // Only handle requests that don't start with /api or /ws
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        // Skip API and WebSocket endpoints - let controllers handle them
                        if (resourcePath.startsWith("api/") || resourcePath.startsWith("ws/")) {
                            return null; // Let Spring MVC handle it
                        }
                        
                        Resource requestedResource = location.createRelative(resourcePath);
                        
                        // If the resource exists, return it
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }
                        
                        // For SPA routing, try to serve index.html only if it exists
                        ClassPathResource indexResource = new ClassPathResource("/static/index.html");
                        if (indexResource.exists()) {
                            return indexResource;
                        }
                        
                        // If static directory doesn't exist (backend-only deployment), return null
                        // This allows Spring to return 404 instead of error
                        return null;
                    }
                });
    }
}