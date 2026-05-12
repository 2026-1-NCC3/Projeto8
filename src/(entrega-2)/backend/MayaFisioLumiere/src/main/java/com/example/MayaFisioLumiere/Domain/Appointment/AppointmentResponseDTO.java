package com.example.MayaFisioLumiere.Domain.Appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID appointment_id,
        LocalDateTime date,
        String time,
        String description,
        UUID patient_id
        ) {

}
