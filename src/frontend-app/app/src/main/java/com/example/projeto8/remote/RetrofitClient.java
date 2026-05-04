package com.example.projeto8.remote;

import android.content.Context;

import com.example.projeto8.api.appointment.AppointmentService;
import com.example.projeto8.api.exerciseSession.ExerciseSessionService;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.api.workout.WorkoutService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
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
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        // LÓGICA: Se NÃO for login, tenta adicionar o token
                        String path = originalRequest.url().encodedPath();
                        if (!path.contains("/api/patient/login") && !path.contains("/api/auth/login")) {

                            String token = appContext.getSharedPreferences("STORAGE", Context.MODE_PRIVATE)
                                    .getString("token", "");

                            if (token != null && !token.isEmpty()) {
                                builder.addHeader("Authorization", "Bearer " + token);
                            }
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
        public static AppointmentService getAppointmentService() {
            return getRetrofitInstance().create(AppointmentService.class);
        }

        /*public static AppointmentService getAppointmentService(){
        return getRetrofitInstance().create(getAppointmentService.class);
        } */


}
