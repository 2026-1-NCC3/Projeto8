package com.example.projeto8.model;

public class Task {
    private String name;
    public boolean isExpanded = false; // Necessário para o clique de expandir
    public boolean isDone = false;     // Necessário para o check verde

    private String title;
    private int serie;
    private int reps;
    private Long exercise_id;
    private String midiaURL;
    private String description;
    private String tags;

    public Task(Long exerciseId, String title, int serie, int reps, String midiaURL, String description) {        this.exercise_id = exercise_id;
        this.title = title;
        this.serie = serie;
        this.reps = reps;
        this.midiaURL = midiaURL;
        this.description = description;
        this.tags = tags;

    }

    // Getters
    public Long getExerciseId() { return exercise_id; }
    public String getTitle() { return title; }
    public int getReps() { return reps; }
    public int getSerie() { return serie; }
    public String getMidiaURL() { return midiaURL; } // NOVO
    public String getDescription() { return description; }
    public String getTags() { return tags; } // NOVO


    //teste dados mockados
    public Task(String name) {
        this.name = name;
    }

    // Getter para o Adapter conseguir ler o nome e mostrar na tela
    public String getName() {
        return name;
    }

}
