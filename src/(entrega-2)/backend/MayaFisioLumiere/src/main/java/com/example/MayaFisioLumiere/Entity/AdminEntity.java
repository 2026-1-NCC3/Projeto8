package com.example.MayaFisioLumiere.Entity;

import com.example.MayaFisioLumiere.Entity.role.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "admin")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminEntity implements UserDetails {

    // reconhece que esse usuário vai ser autenticado dentro da aplicação spring,
    // se estiver dando erro é so implementar os metodos automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminUser_ID;

    @Column(nullable = false)
    private String adminName;

    @Column(nullable = false, unique = true)
    private String adminEmail;

    @Column(nullable = false)
    private String adminPassword;

    @Enumerated(EnumType.STRING) // salva como admin dentro do banco de dados
    private UserRole role;

    // adicionar essas colunas dentro do banco de dados para expiração da sessão do admin
    @Column(name = "total_minutes_today")
    private long totalMinutesUsedToday = 0;

    @Column(name = "last_request_time")
    private LocalDateTime lastRequestTime;

    @Column(name = "last_access_date")
    private LocalDate lastAccessDate = LocalDate.now();

    // anotações geradas automaticamente ao implementarmos a classe de UserDetails
    // essa classe a baixo diz sobre o tipo de permissão que estamos dando para o nosso admin
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority(UserRole.ADMIN.getRole()), // Returns "ROLE_ADMIN"
                    new SimpleGrantedAuthority(UserRole.PATIENT.getRole()) // Returns "ROLE_PATIENT"
            );
        }
        return List.of(new SimpleGrantedAuthority(UserRole.PATIENT.getRole()));
    }

    @Override
    public @Nullable
    String getPassword() {
        return adminPassword;
    }

    @Override
    public String getUsername() {
        return adminEmail;
    }

    @Override
    public boolean isEnabled() {
        /* logica de tempo de sessão por dia
        if (lastAccessDate != null && !lastAccessDate.equals(LocalDate.now())) {
            return true;
        }

        int limitMinutes = (this.role == UserRole.Admin) ? 540 : 180; //9h de sessão ou 3h de sessão, tem que ver quanto tempo a gente pretende colocar para a sessão diaria do admin e do usuário
        return this.totalMinutesUsedToday < limitMinutes;*/
        return true;
    }

    // verifica se a conta não está expirada
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // conta não bloqueada tem que implementar uma lógica ainda pra ver se ela fica expirada ou não
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // credenciais não expiradas fazer a lógica de implementação ainda delas, com base no jwt e hash
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
