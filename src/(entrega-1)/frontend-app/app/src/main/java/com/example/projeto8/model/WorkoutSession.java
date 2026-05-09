package com.example.projeto8.model;

import java.time.LocalDate;
import java.util.List;

public class WorkoutSession {
    private Long workoutSession_id;
    private String weekDay;
    private Boolean checked;
    private LocalDate workoutDate;

    public Boolean getChecked() {
        return checked;
    }

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    // ADICIONE ESTA LINHA:
    private String patient;

    // ADICIONE ESTA LISTA (para os exercícios como agachamento):
    private List<ExerciseSession> exercises;

    // ADICIONE ESTES GETTERS (O Android precisa deles para ler os dados):
    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
    public List<ExerciseSession> getExercises() {
        return exercises;
    }

    public String getWeekDay() { return weekDay; }
}
