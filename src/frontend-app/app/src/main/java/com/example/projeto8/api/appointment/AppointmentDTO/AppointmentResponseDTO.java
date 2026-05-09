package com.example.projeto8.api.appointment.AppointmentDTO;

import java.util.UUID;

public class AppointmentResponseDTO {

    private UUID appointment_id;
    private UUID patient_id;
    private String description;
    private String time;
    private String date; //LocalDate

    public AppointmentResponseDTO() {
    }

    public UUID getAppointment_id() {
        return appointment_id;
    }

    public UUID getPatient_id() {
        return patient_id;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
