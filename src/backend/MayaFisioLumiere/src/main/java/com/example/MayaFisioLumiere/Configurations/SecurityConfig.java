package com.example.MayaFisioLumiere.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //configurações de segurança para utilizarmos no admin e paciente e o spring security funcionar
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // configuração necessária para as api's funcionarem
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // para não ser necessário logar no render
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login/admin").permitAll()    // rota de login de admin
                        .requestMatchers(HttpMethod.POST, "/auth/register/admin").hasAuthority("admin")// rota de sing-up de admin
                        .requestMatchers(HttpMethod.POST, "/auth/register/patient").hasAuthority("admin") // rota de sign-in paciente, permitindo com que o admin cadastre ele
                        .requestMatchers(HttpMethod.POST, "/auth/login/patient").permitAll() // rota de login do paciente
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()         // para que o cors reconheça as rotas
                        .anyRequest().authenticated()                                   // tudo pede um token para autenticação
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); //necessário para pegarmos as configurações de segurança
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //para conseguirmos utilizar o bcrypt na rota de admin
    }
}