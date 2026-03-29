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

    //Para pegar as exercise sesion do patient, sem ser a workout do dia
    @GET("api/exerciseSession/getAllExerciseSessions")
    Call<List<ExerciseSession>> getAllExerciseSessions();

    // Rota para atualizar a dor do patient
    @PUT("api/exerciseSession/updateExerciseSessionPain/{id}")
    Call<ExerciseSession> updateExerciseSessionPain(
            @Path("id") Long id,
            @Body ExerciseSessionRequestDTO data
    );

    //Colocar aqui a rota de pegar as exerciseSession do patient por id dele
}
