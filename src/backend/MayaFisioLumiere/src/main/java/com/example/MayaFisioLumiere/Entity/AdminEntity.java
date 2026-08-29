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

    // Reconhece que esse usuário vai ser autenticado dentro da aplicação spring,
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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "total_minutes_today")
    private long totalMinutesUsedToday = 0;

    @Column(name = "last_request_time")
    private LocalDateTime lastRequestTime;

    @Column(name = "last_access_date")
    private LocalDate lastAccessDate = LocalDate.now();

    // Anotações geradas automaticamente ao implementarmos a classe de UserDetails
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
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
