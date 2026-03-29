package com.example.projeto8.api.patient.PatientDTO;

public class PatientLoginRequestDTO {

    private String email;
    private String password;

    public PatientLoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
