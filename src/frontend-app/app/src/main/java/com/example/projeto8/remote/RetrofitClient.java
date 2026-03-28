package com.example.projeto8.remote;

import com.example.projeto8.api.exerciseSession.ExerciseSessionService;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.api.workout.WorkoutService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 1. Aqui vai o link que o Render te deu (precisa terminar com /)
  /*  private static final String BASE_URL = "https://projeto8.onrender.com/";
    private static Retrofit retrofit = null;

    public static WorkoutService getWorkoutService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Converte o JSON do banco para Java
                    .build();
        }
        return retrofit.create(WorkoutService.class);
    }

    */
        private static final String BASE_URL = "https://projeto8.onrender.com/";
        private static Retrofit retrofit = null;

        // Dessa forma, é necessário criar o retrofit builder só uma vez invés de para cada rota!!
        private static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

        //Interfaces
        public static WorkoutService getWorkoutService() {
            return getRetrofitInstance().create(WorkoutService.class);
        }

        public static PatientService getPatientService() {
            return getRetrofitInstance().create(PatientService.class);
        }

        public static ExerciseSessionService getExerciseService() {
            return getRetrofitInstance().create(ExerciseSessionService.class);
        }

}
