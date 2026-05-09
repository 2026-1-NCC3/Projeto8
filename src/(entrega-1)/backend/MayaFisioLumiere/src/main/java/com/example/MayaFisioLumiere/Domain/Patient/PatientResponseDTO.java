package com.example.MayaFisioLumiere.Domain.Patient;

import java.util.UUID;

public record PatientResponseDTO(
        UUID patient_id,
        String password,
        String status,
        String name,
        String surname,
        String email,
        String birthDate,
        String cellPhone,
        String gender,
        Double height,
        Double weight,
        boolean lgpdCheck,
        String description,
        String cpf

) {
}
