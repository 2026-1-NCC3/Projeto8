package com.example.projeto8.api.workout;
import java.util.List;
import java.util.UUID;

import com.example.projeto8.model.WorkoutSession;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WorkoutService {

    // Busca a lista de exercícios pelo ID do paciente
    @GET("api/workout/patient/{patient_id}")
    Call<List<WorkoutSession>> getWorkoutsByPatient(@Path("patient_id") String patientId);



}
