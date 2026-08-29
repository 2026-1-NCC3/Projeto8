package com.example.MayaFisioLumiere.Entity.role;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    PATIENT("ROLE_PATIENT");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
