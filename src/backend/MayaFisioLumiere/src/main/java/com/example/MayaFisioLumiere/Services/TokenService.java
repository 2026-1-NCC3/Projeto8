package com.example.MayaFisioLumiere.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.MayaFisioLumiere.entity.AdminEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    // Agora aceita UserDetails (funciona para AdminEntity e PatientEntity)
    public String generateToken(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Esta lógica extrai apenas o NOME da primeira permissão
            // Exemplo: Transforma [admin, user] em apenas "admin"
            String role = user.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElse("user");

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withClaim("role", role) // Agora o token terá "role": "admin"
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }


    }

    public Long getExpirationDate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getExpiresAt() // Pega o objeto Date da expiração
                    .getTime();    // Converte para milissegundos (Long)
        } catch (JWTVerificationException exception) {
            // Se o token já for inválido, retornamos o tempo atual para ele expirar logo
            return System.currentTimeMillis();
        }
    }

    // o quanto de tempo a sessão desse usuario vai expirar
    private Instant genExpirationDate() {
        // vai expirar em 2h a partir da entrada da aplicação
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}