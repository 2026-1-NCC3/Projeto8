package com.example.projeto8.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

    private UUID appointment_id;
    private String date;
    private String time;
    private String description;
    private UUID patient_id;

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

    public UUID getPatientAppt() {
        return patient_id;
    }

}
