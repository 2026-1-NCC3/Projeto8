
package com.example.projeto8.model;

import com.example.projeto8.api.exerciseSession.ExerciseSessionDTO.ExerciseResponseDTO;
import com.example.projeto8.model.Exercise;
import com.google.gson.annotations.SerializedName;

public class ExerciseSession {

    @SerializedName("exercisesession_id")
    private Long exercisesession_id;

    @SerializedName("exercise_id")
    private Long exerciseId;

    @SerializedName("name") // Se o back enviar o nome
    private String name;
    private ExerciseResponseDTO exercise;
    private int serie;
    private int repetitions;
    private boolean feelPain;


    // O @SerializedName garante que o Android entenda se o Back enviar "name"

    public Long getExercisesession_id() {
        return exercisesession_id;
    }


    public ExerciseResponseDTO getExercise() {
        return exercise;
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
