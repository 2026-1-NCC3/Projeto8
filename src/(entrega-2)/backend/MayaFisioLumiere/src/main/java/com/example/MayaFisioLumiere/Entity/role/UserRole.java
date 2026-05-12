package com.example.MayaFisioLumiere.Entity.role;

// aqui a gente define qual o papel dos nossos usuários como visto no doc abaixo pus como admin(maya ou mais fisioterapeutas) e paciente
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
