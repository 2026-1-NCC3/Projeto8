package com.example.projeto8.model;

import com.google.gson.annotations.SerializedName;

public class Exercise {

    @SerializedName("exercise_id")
    private Long exercise_id;

    @SerializedName("title")
    private String title;

    @SerializedName("midiaURL")
    private String midiaURL;

    @SerializedName("tags")
    private String tags;

    @SerializedName("description")
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
