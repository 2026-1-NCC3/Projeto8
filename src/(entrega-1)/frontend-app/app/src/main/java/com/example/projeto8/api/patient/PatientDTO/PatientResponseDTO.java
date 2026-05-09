package com.example.projeto8.api.patient.PatientDTO;

import java.util.UUID;

public class PatientResponseDTO {
    private UUID patient_ID;
        private String name;
        private String surname;
        private String email;
        private String cellPhone;
        private String cpf;
        private String password;
        private int patientAge;
        private String birthDate;
        private String status;
        private String gender;
        private String height;
        private String weight;
        private boolean lgpdCheck;
        private String description;


        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getEmail() {
            return email;
        }

        public String getCpf() {
            return cpf;
        }

        public String getPassword() {
            return password;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public String getStatus() {
            return status;
        }

        public String getGender() {
            return gender;
        }

        public int getPatientAge() {
            return patientAge;
        }

        public boolean isLgpdCheck() {
            return lgpdCheck;
        }

        public String getDescription() {
            return description;
        }

        public String getCellPhone() {
            return cellPhone;
        }

        public String getHeight() {
            return height;
        }

        public String getWeight() {
            return weight;
        }
    }

