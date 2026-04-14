package com.example.MayaFisioLumiere.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "appointment")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID appointment_id;

    @Column( name = "date", nullable = false)
    private LocalDateTime date;

    @Column
    private String time;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "admin_user_id")
    private AdminEntity admin;
}