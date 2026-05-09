package com.example.MayaFisioLumiere.Domain.WorkoutSession;

import java.util.List;
import java.util.UUID;

public record WorkoutSesRequestDTO(
        String weekDay,
        Boolean checked,
        UUID patient_id
) {
}
