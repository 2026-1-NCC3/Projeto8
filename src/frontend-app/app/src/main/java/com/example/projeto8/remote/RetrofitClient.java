package com.example.projeto8.remote;

import android.content.Context;

import com.example.projeto8.api.exerciseSession.ExerciseSessionService;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.api.workout.WorkoutService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;
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
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            if (appContext == null) {
                throw new RuntimeException("RetrofitClient deve ser inicializado com init(context) antes do uso.");
            }

            // 1. INTERCEPTOR DE LOG (Para ver o erro no Logcat)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2. CONFIGURAÇÃO DO CLIENTE OKHTTP
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging) // Adiciona o Log primeiro
                    .addInterceptor(chain -> {
                        // 3. INTERCEPTOR DE TOKEN (Seu código original)
                        String token = appContext.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
                                .getString("token", "");

                        Request.Builder builder = chain.request().newBuilder();
                        if (token != null && !token.isEmpty()) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }
                        return chain.proceed(builder.build());
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
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
