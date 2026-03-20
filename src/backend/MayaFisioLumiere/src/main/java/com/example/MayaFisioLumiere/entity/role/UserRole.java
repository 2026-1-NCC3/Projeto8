package com.example.MayaFisioLumiere.entity.role;

// aqui a gente define qual o papel dos nossos usuários como visto no doc abaixo pus como admin(maya ou mais fisioterapeutas) e paciente
public enum UserRole {
    Admin("admin"),
    Patient("patient");

    private String role;

     UserRole(String role){
         this.role = role;
     }

     public String getRole(){
         return role;
     }
}
