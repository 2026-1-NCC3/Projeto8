package com.example.projeto8.model;

import java.util.UUID;

import retrofit2.http.GET;

public class Patient {

    private UUID patient_ID;
    private String name;
    private String surname;
    private String email;
    private String cpf;
    private String password;
    private String birthDate;
    private String status;
    private String gender;
    private String height;

    private String weight;

    public String getName() {
        return name;
    }


}