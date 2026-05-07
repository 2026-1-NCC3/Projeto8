package com.example.projeto8.api.workout;
import java.util.List;
import java.util.UUID;

import com.example.projeto8.model.WorkoutSession;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WorkoutService {

    // Busca a list
    // a de exercícios pelo ID do paciente
    @GET("api/workout/patient/{patient_id}")
    Call<List<WorkoutSession>> getWorkoutsByPatient(@Path("patient_id") String patientId);

    // Rota para dar o "Check" final no treino e atualizar o status do paciente para ATIVO
    @PUT("api/workout/check/{id}")
    Call<Void> checkWorkout(@Path("id") Long workoutId);
}
