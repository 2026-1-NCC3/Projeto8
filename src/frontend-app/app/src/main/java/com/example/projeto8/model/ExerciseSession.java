package com.example.projeto8.model;

import com.example.projeto8.model.Exercise;
import com.google.gson.annotations.SerializedName;

public class ExerciseSession {

    // O @SerializedName garante que o Android entenda se o Back enviar "name"
    @SerializedName("name")
    private String name;

    private Long exercisesession_id;
    private int serie;
    private int repetitions;
    private Boolean feelPain;
    private Long workoutSession;
    private String patient;
    private Exercise exercise;

    public Long getExercisesession_id() {
        return exercisesession_id;
    }

    public int getSerie() {
        return serie;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public Boolean getFeelPain() {
        return feelPain;
    }

    public void setFeelPain(Boolean feelPain) {
        this.feelPain = feelPain;
    }

    public Long getWorkoutSession() {
        return workoutSession;
    }

    public String getPatient() {
        return patient;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public ExerciseSession() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
