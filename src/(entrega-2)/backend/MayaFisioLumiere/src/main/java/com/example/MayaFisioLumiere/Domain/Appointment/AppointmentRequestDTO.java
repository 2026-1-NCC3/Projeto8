package com.example.MayaFisioLumiere.Domain.Appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
        LocalDateTime date,
        String time,
        UUID patient_id,
        String description
        ) {

}
