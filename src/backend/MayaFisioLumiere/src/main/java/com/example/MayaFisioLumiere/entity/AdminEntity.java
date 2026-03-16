package com.example.MayaFisioLumiere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "admin")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminUser_ID;

    @Column(nullable = false)
    private String adminName;


    @Column(nullable = false, unique = true)
    private String adminEmail;

    @Column(nullable = false)
    private String adminPassword;

    //Getters para get no login e logout, creio que setters não são necessários porque setamos os valores no Service
    public Long getAdminUser_ID() {
        return adminUser_ID;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
