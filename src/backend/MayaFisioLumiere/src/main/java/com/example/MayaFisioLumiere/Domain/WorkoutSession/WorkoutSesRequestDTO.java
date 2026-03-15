package com.example.MayaFisioLumiere.Domain.WorkoutSession;

public record WorkoutSesRequestDTO(
        String weekDay,
        Long patient,
        Long exercise
) {
}
