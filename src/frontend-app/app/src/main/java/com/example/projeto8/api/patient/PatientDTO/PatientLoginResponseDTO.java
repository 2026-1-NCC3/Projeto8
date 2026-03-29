package com.example.projeto8.api.patient.PatientDTO;

import com.google.gson.annotations.SerializedName;

public class PatientLoginResponseDTO {

    private Long id;
    private String name;
    private String email;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @SerializedName("token")
    private String token;

    public String getToken() { return token; }
}
