package com.example.MayaFisioLumiere.Domain.Patient;

import java.util.UUID;

public record PatientResponseDTO(
        UUID patient_id,
        String password,
        String status,
        String name,
        String surname,
        String email
) {
}
