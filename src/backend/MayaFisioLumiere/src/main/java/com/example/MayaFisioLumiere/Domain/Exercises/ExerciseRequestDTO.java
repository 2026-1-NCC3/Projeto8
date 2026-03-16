package com.example.MayaFisioLumiere.Domain.Exercises;

public record ExerciseRequestDTO(
        String title,
        String midiaURL,
        String tags,
        String description) {
}
