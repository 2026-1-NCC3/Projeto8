package com.example.MayaFisioLumiere.Domain.ExerciseSession;

import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;

import java.util.UUID;

public record ExerciseSessionResponseDTO(
        int exercisesession_id,
        ExerciseResponseDTO exercise,
        Long workoutSession,
        UUID patient_id,
        int serie,
        int repetitions,
        Boolean feelPain
) {

}
