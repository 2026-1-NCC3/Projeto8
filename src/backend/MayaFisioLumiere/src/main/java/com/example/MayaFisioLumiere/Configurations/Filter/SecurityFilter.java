package com.example.MayaFisioLumiere.Configurations.Filter;

import com.example.MayaFisioLumiere.Services.TokenService;
import com.example.MayaFisioLumiere.repository.AdminRepository;
import com.example.MayaFisioLumiere.repository.PatientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// classe pra ajudar a filtrar as coisas dentro do banco de dados e ver por role, sendo admin ou paciente
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminRepository adminRepository; // Renomeado para clareza

    @Autowired
    private PatientRepository patientRepository; // Adicionado para reconhecer pacientes

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        var token = this.recoverToken(request);
        if (token != null) {
            var email = tokenService.validateToken(token);
            if (email != null) {
                UserDetails user = null;

                var admin = adminRepository.findByAdminEmail(email);
                if (admin.isPresent()) {
                    user = admin.get();
                } else {
                    var patient = patientRepository.findByEmail(email);
                    if (patient.isPresent()) {
                        user = patient.get();
                    }
                }

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        String token = authHeader.substring(7);
        if (token.equals("undefined") || token.equals("null") || token.isBlank()) return null;

        return token;
    }
}