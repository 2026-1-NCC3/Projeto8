package com.example.projeto8.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 1. Aqui vai o link que o Render te deu (precisa terminar com /)
    private static final String BASE_URL = "https://projeto8.onrender.com/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Converte o JSON do banco para Java
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
