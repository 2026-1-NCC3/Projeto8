package com.example.projeto8.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

    private UUID appointment_id;
    private LocalDateTime date;
    private String time;
    private String description;
    private Patient patient;

    public UUID getAppointment_id() {
        return appointment_id;
    }

    public LocalDateTime getDate() {
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
