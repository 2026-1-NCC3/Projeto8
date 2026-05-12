package com.example.projeto8.api.exerciseSession;

import com.example.projeto8.api.exerciseSession.ExerciseSessionDTO.ExerciseSessionRequestDTO;
import com.example.projeto8.model.ExerciseSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExerciseSessionService {

    @GET("api/exerciseSession/getAllExerciseSessions")
    Call<List<ExerciseSession>> getAllExerciseSessions();

    // Rota para atualizar a dor do patient
    @PUT("api/exerciseSession/updateExerciseSessionPain/{patient_id}/{exerciseSession_id}")
    Call<Void> updateExerciseSessionPain(
            @Path("patient_id") String patientId,
            @Path("exerciseSession_id") Long exerciseSessionId,
            @Body ExerciseSessionRequestDTO data
    );
}
