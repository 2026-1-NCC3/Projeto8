package com.example.projeto8.model;

import com.google.gson.annotations.SerializedName;

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

    private String patient;

    @SerializedName("exercises")
    private List<ExerciseSession> exerciseSessions;

    public String getPatient() {
        return patient;
    }

    public List<ExerciseSession> exerciseSessions() {
        return exerciseSessions;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public List<ExerciseSession> getExercises() {
        return exerciseSessions;
    }

    public Long getWorkoutSession_id() {
        return workoutSession_id;
    }

}
