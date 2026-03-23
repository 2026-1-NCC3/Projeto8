package com.example.MayaFisioLumiere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exercisesession")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExerciseSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exercisesession_id;

    @Column(nullable = false)
    private int serie;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = true)
    private Boolean feelPain; //DEPOIS, COLOCAR PARA FALSE, ERRO DE POSTGRES NAO IDENTIFICANDO

    @ManyToOne
    @JoinColumn(name="workoutsession_id", nullable= false)
    private WorkoutSessionEntity workoutSession;

    @ManyToOne
    @JoinColumn(name="patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name="exercise_id", nullable = false)
    private ExerciseEntity exercise;


}
