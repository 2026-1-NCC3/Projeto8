package com.example.MayaFisioLumiere.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name="workoutSession")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long workoutSession_id;

    @Column(nullable = false)
    private String weekDay;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int checked;

    @ManyToOne
    @JoinColumn(name="patient_id",nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name="exercisesession_id", nullable = false)
    private ExerciseEntity exercise;
}
