package com.example.projeto8.model;

public class Exercise {
    //Para pegar os dados de exercicios

    private Long exercise_id;
    private String title;
    private String midiaURL;
    private String tags;
    private String description;

    public Long getExercise_id() {
        return exercise_id;
    }

    public String getTitle() {
        return title;
    }

    public String getMidiaURL() {
        return midiaURL;
    }

    public String getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }
}
