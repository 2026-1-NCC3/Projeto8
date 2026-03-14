package com.example.MayaFisioLumiere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

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
    private String serie;

    @ManyToOne
    @JoinColumn(name="workoutsession_id", nullable= false)
    private WorkoutSessionEntity workoutSession_id;

    @ManyToOne
    @JoinColumn(name="patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name="exercise_id", nullable = false)
    private ExerciseEntity exercise;


}
