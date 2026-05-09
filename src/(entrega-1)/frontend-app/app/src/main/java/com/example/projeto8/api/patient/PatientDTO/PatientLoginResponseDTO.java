package com.example.projeto8.api.patient.PatientDTO;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class PatientLoginResponseDTO {

    @SerializedName("patient_id")
    private String patient_id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;
    @SerializedName("token")
    private String token;

    @SerializedName("lgpd_check")
    private boolean lgpdCheck;

    public String getId() { return patient_id; }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getToken() { return token; }

    public boolean isLgpdCheck() {
        return  lgpdCheck;
    }
}
