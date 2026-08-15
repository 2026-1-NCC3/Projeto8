package com.example.MayaFisioLumiere.Entity;

import com.example.MayaFisioLumiere.Entity.role.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patient_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID patient_ID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int patientAge;

    @Column(nullable = false)
    private String birthDate;

    @Column(nullable = true, name = "lgpd_check")
    private boolean lgpdCheck;

    @Column(nullable = false)
    private String status = "INATIVO";

    @Column(nullable = true)
    private String cellPhone;

    @Column(nullable = true)
    private String gender;

    @Column
    private Double height;

    @Column
    private Double weight;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "total_minutes_today")
    private long totalMinutesUsedToday = 0;

    @Column(name = "last_request_time")
    private LocalDateTime lastRequestTime;

    @Column(name = "last_access_date")
    private LocalDate lastAccessDate = LocalDate.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_PATIENT"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_PATIENT"));
        }
    }

    @Override
    public @Nullable
    String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
