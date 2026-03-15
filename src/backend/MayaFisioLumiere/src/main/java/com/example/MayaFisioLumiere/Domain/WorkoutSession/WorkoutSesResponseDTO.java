package com.example.MayaFisioLumiere.Domain.WorkoutSession;

public record WorkoutSesResponseDTO(
        Long workoutSession_id,
        String weekDay,
        Long patient,
        Long exercise
) {

}
