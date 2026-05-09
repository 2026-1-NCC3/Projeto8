package com.example.MayaFisioLumiere.Domain.ExerciseSession;

import java.util.UUID;

public record ExerciseSessionRequestDTO(
        Long exercise_id,
        Long workoutSession,
        UUID patient_id,
        Integer serie,
        Integer repetitions,
        Boolean feelPain
        ) { }
