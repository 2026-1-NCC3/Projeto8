package com.example.MayaFisioLumiere.entity;

import com.example.MayaFisioLumiere.entity.role.UserRole;
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
@Table(name = "patients") //seleciona a tabela de pacientes do banco de dados
@Getter // get automatico nos argumentos abaixo
@Setter // set automatico nos argumentos abaixo
@NoArgsConstructor // construtor sem argumentos (usado para o pacote do spring jpa)
@AllArgsConstructor // construtor com todos os campos
public class PatientEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //gera uuid automatica de acordo com a tabela de pacientes
    private UUID patient_ID;

    @Column(nullable = false)//tipo da coluna no banco de dados e nas outras tb
    private String name;

    @Column(nullable=false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column( nullable = false)
    private String password;

    @Column(nullable = true)
    private String birthDate; //DEPOIS, COLOCAR PARA FALSE, ERRO DE POSTGRES NAO IDENTIFICANDO

    @Column(nullable = false)
    private String status = "INATIVO";

    //ADICIONAR numero zap, genero, altura, peso

    @Column( nullable = false)
    private String cellPhone;

    @Column( nullable = false)
    private String gender;

    @Column(precision = 5, scale = 2)
    private Double height;

    @Column(precision = 5, scale = 2)
    private Double weight;

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
        if(this.role == UserRole.Patient) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_PATIENT"));
        else return List.of(new SimpleGrantedAuthority("ROLE_PATIENT"));
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        // logica de tempo de sessão por dia
        if (lastAccessDate != null && !lastAccessDate.equals(LocalDate.now())) {
            return true;
        }

        int limitMinutes = (this.role == UserRole.Patient) ?  180: 60; //3h de sessão ou 1h de sessão, tem que ver quanto tempo a gente pretende colocar para a sessão diaria do admin e do usuário
        return this.totalMinutesUsedToday < limitMinutes;
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