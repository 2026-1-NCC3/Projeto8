package com.example.MayaFisioLumiere.Domain.Patient;

public record PatientRequestDTO(
        String name,
        String surname,
        String cpf,
        String email,
        String password,
        int patientAge
) { }
