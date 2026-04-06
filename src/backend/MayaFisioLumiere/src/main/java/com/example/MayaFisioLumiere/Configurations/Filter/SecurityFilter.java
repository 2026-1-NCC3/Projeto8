package com.example.MayaFisioLumiere.Configurations.Filter;

import com.example.MayaFisioLumiere.Services.TokenService;
import com.example.MayaFisioLumiere.Repository.AdminRepository;
import com.example.MayaFisioLumiere.Repository.PatientRepository;
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

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Trata pre-flight do CORS (Evita 403 em requisições de navegadores/mobile)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        var token = this.recoverToken(request);

        // 2. Se não houver token, segue o fluxo (O SecurityConfig decidirá se a rota é permitAll)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var login = tokenService.validateToken(token);

        if (login != null) {
            // 3. Tenta encontrar como ADMIN
            var adminOptional = adminRepository.findByAdminEmail(login);

            if (adminOptional.isPresent()) {
                setAuthentication(adminOptional.get());
            } else {
                // 4. Se não for admin, tenta encontrar como PACIENTE
                var patientOptional = patientRepository.findByEmail(login);
                if (patientOptional.isPresent()) {
                    setAuthentication(patientOptional.get());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserDetails user) {
        var authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        String token = authHeader.substring(7).trim();
        if (token.isEmpty() || token.equals("undefined") || token.equals("null")) return null;

        return token;
    }
}