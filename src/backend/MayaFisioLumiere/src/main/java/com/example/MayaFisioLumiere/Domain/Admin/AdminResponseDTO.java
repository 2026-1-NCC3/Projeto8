package com.example.MayaFisioLumiere.Domain.Admin;

public record AdminResponseDTO(
        Long adminUser_ID,
        String adminName,
        String adminEmail,
        String adminPassword ) {
}
