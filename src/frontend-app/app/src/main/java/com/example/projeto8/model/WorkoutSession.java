package com.example.projeto8.model;

import java.util.List;

public class WorkoutSession {
    private Long workoutSession_id;
    private String weekDay;
    private Boolean checked;

    // ADICIONE ESTA LINHA:
    private Patient patient;

    // ADICIONE ESTA LISTA (para os exercícios como agachamento):
    private List<ExerciseSessionEntity> exerciseSessions;

    // ADICIONE ESTES GETTERS (O Android precisa deles para ler os dados):
    public Patient getPatient() {
        return patient;
    }

    public List<ExerciseSessionEntity> getExerciseSessions() {
        return exerciseSessions;
    }

    public String getWeekDay() { return weekDay; }
}
