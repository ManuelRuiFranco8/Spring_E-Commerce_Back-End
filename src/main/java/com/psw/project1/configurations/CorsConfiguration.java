package com.psw.project1.configurations;

import org.springframework.context.annotation.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config=new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); //allows requests from any origins
        config.addAllowedHeader("*"); //allows requests to include any headers
        config.addAllowedMethod("DELETE"); //allows http delete method
        config.addAllowedMethod("GET"); //allows http get method
        config.addAllowedMethod("POST"); //allows http post method
        config.addAllowedMethod("PUT"); //allows http put method
        source.registerCorsConfiguration("/**", config); //applies CORS configuration to all URLs in the app
        return new CorsFilter(source);
    }//corsFilter (defines CORS configuration)
}//CorsConfiguration