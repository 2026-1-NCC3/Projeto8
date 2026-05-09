package com.example.projeto8.api.exerciseSession.ExerciseSessionDTO;

public class ExerciseResponseDTO {
    private Long exercise_id;
    private String title;
    private String midiaURL;
    private String tags;
    private String description;

    // Construtor vazio para o Retrofit/Gson
    public ExerciseResponseDTO() {}

    // Getters
    public Long getExercise_id() { return exercise_id; }

    public String getTitle() { return title; }

    public String getMidiaURL() { return midiaURL; }
    public String getTags() { return tags; }
    public String getDescription() { return description; }
}

