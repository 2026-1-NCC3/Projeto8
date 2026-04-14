package com.example.projeto8.api.patient;

import com.example.projeto8.api.patient.PatientDTO.PatientLoginResponseDTO;
import com.example.projeto8.model.Patient;
import com.example.projeto8.api.patient.PatientDTO.PatientLoginRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PatientService {
    //para pegar as rotas do backend de patients (dados do patient)!
    //Buscar os dados do paciente pelo nome (para o profile)
    @GET("api/patient/getByName/{name}")
    Call<List<Patient>> getPatientByFullName(@Path("name") String name, @Query("surname") String surname);

    
    //Login do patient
    @Headers({"User-Agent: Mozilla/5.0", "Content-Type: application/json"})
    @POST("api/patient/login")
    Call<PatientLoginResponseDTO> login(@Body PatientLoginRequestDTO data);

    @PUT("api/patient/updateLgpdStatus/{id}")
    Call<Void> updateLgpdStatus(@Path("id") String id, @Query("lgpdCheck") boolean lgpdCheck);

    //LOGIN PARA USO DURANTE TESTES E DEVELOPMENT:
    //analice.coimbra@lumiere.com
    // 2004-07-14

}
