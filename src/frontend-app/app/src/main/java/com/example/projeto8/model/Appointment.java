package com.example.projeto8.model;

import java.time.LocalDate;
import java.util.UUID;

public class Appointment {
    private UUID appointment_id;
    private UUID patient_id;
    private String description;
    private String time;
    private String date; //Antes era LocalDate

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
