
package com.example.projeto8.model;

import com.example.projeto8.model.Exercise;
import com.google.gson.annotations.SerializedName;

public class ExerciseSession {

    // O @SerializedName garante que o Android entenda se o Back enviar "name"
    @SerializedName("name")
    private String name; //nao seria melhor pegar pelo exercise ID dentro da exercise session?


    private Long exercisesession_id;
    private int serie;
    private int repetitions;
    private Boolean feelPain;
    private WorkoutSession workoutSession;
    private Patient patient;
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

    public WorkoutSession getWorkoutSession() {
        return workoutSession;
    }

    public Patient getPatient() {
        return patient;
    }

    public Exercise getExercise() {
        return exercise;
    }

    // Construtor vazio (necessário para o Retrofit)
    public ExerciseSession() {}

    // Getter para pegarmos o nome do exercício na MainActivity
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
