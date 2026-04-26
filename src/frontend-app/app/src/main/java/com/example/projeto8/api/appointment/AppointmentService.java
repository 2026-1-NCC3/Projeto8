package com.example.projeto8.api.appointment;

import com.example.projeto8.model.Appointment;
import com.example.projeto8.model.ExerciseSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AppointmentService {

    @GET("/api/appointment/patient/{uuid}")
    Call<List<Appointment>> getAppointmentsByPatient();
}
