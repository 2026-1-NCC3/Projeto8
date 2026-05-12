package com.example.MayaFisioLumiere.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "workoutSession")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long workoutSession_id;

    @Column(nullable = false)
    private String weekDay;

    @Column(nullable = false)
    private Boolean checked = false;

    @Column(name = "workout_date")
    private LocalDate workoutDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @JsonManagedReference
    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ExerciseSessionEntity> exerciseSessions;

}
