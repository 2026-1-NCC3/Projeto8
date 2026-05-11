package com.example.projeto8.api.exerciseSession.ExerciseSessionDTO;

public class ExerciseSessionRequestDTO {

    private Boolean feelPain;

    public ExerciseSessionRequestDTO(Boolean feelPain) {
        this.feelPain = feelPain;
    }

    public Boolean getFeelPain() {
        return feelPain;
    }

    public void setFeelPain(Boolean feelPain) {
        this.feelPain = feelPain;
    }
}
