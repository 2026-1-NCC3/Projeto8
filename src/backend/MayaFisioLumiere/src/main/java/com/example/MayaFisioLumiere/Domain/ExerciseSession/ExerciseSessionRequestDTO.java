package com.example.MayaFisioLumiere.Domain.ExerciseSession;

import java.util.UUID;

public record ExerciseSessionRequestDTO(
        Long exercise_id,
        Long workoutsession_id,
        UUID patient_id,
        String serie
        ) { }
