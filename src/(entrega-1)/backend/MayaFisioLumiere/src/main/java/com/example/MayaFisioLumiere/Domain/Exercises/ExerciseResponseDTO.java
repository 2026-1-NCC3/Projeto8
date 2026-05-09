package com.example.MayaFisioLumiere.Domain.Exercises;

public record ExerciseResponseDTO(
        Long exercise_id,
        String title,
        String midiaURL,
        String tags,
        String description) {
}
