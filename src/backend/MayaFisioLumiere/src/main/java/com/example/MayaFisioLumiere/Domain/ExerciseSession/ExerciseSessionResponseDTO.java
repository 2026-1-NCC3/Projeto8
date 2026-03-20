package com.example.MayaFisioLumiere.Domain.ExerciseSession;

import java.util.UUID;

public record ExerciseSessionResponseDTO(
        int exercisesession_id,
        Long exercise_id,
        Long workoutSession,
        UUID patient_id,
        int serie,
        int repetitions,
        Boolean feelPain
) {

}
