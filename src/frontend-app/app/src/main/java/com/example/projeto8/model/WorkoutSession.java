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
    private Patient patient;

    // ADICIONE ESTA LISTA (para os exercícios como agachamento):
    private List<ExerciseSession> exerciseSessions;

    // ADICIONE ESTES GETTERS (O Android precisa deles para ler os dados):
    public Patient getPatient() {
        return patient;
    }

    public List<ExerciseSession> getExerciseSessions() {
        return exerciseSessions;
    }

    public String getWeekDay() { return weekDay; }
}
