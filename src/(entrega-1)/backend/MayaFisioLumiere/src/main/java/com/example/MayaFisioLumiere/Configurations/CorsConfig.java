package com.example.MayaFisioLumiere.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns( "*"

                        /* Quando o aplicativo estiver live podemos manter essas rotas restritas, enquanto não está,
                         precisa estar liberado todas as rotas porque o Android Studio NAO TEM ROTA ESPECIFICA!
                        allowedOrigins
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "http://localhost:8081",
                        "https://lumiere-project8.vercel.app"
                        */
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}