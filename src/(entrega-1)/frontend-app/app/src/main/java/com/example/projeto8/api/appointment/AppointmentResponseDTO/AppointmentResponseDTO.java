package com.example.projeto8.api.appointment.AppointmentResponseDTO;

import com.example.projeto8.model.Patient;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentResponseDTO {

    private UUID appointment_id;
    private String date;
    private String time;
    private String description;
    private Patient patient;

    public AppointmentResponseDTO(){

    }

    public UUID getAppointment_id() {
        return appointment_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public Patient getPatientAppt() {
        return patient;
    }
}
