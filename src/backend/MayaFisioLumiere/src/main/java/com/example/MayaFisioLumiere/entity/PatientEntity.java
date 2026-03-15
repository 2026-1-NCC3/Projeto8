package com.example.MayaFisioLumiere.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "patients") //seleciona a tabela de pacientes do banco de dados
@Getter // get automatico nos argumentos abaixo
@Setter // set automatico nos argumentos abaixo
@NoArgsConstructor // construtor sem argumentos (usado para o pacote do spring jpa)
@AllArgsConstructor // construtor com todos os campos
public class PatientEntity {
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

    @Column(unique = false, nullable = false)
    private int patientAge;

    @Column(nullable = false)
    private String status;

}