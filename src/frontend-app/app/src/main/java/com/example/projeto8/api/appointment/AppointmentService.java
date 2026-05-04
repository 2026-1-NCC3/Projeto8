package com.example.projeto8.api.appointment;

import com.example.projeto8.model.Appointment;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppointmentService {

    @GET("api/appointment/patient/{patient_id}")
    Call<List<Appointment>> getAppointmentByPatient(@Path("patient_id") UUID patient_id);

}
