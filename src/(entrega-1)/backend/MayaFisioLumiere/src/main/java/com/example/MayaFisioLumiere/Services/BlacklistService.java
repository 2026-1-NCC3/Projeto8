package com.example.MayaFisioLumiere.Services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlacklistService {
    // chave: jwt token criado | valor: data de expiração (System.currentTimeMillis())
    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    //adiciona o token a um tipo de lista negra que funciona pra encerrar a sessão do usuário
    // token jwt puro,  expirationTime tempo em milissegundos
  /**
    *  @param token
   * @param expirationTime
  **/
  public void addTokenToBlacklist(String token, Long expirationTime) {
        blacklistedTokens.put(token, expirationTime);
    }


    // Verifica se o token está invalidado.
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    // tira os tokens utilizados por hora
    @Scheduled(fixedRate = 3600000) // pode ajustar pra ficar de acordo com o que quisermos
    public void cleanExpiredBlacklist() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
        System.out.println("Blacklist limpa: tokens expirados removidos.");
    }
}