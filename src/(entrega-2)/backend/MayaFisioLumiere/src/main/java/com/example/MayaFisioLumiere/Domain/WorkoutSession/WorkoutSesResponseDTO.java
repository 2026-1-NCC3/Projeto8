package com.example.MayaFisioLumiere.Domain.WorkoutSession;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;

import java.util.List;
import java.util.UUID;

public record WorkoutSesResponseDTO(
        Long workoutSession_id,
        String weekDay,
        Boolean checked,
        UUID patient,
        List<ExerciseSessionResponseDTO> exercises
        ) {

}
