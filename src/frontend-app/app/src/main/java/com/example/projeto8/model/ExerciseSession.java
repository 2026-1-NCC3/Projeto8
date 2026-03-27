
package com.example.projeto8.model;

import com.google.gson.annotations.SerializedName;

public class ExerciseSession {

    // O @SerializedName garante que o Android entenda se o Back enviar "name"
    @SerializedName("name")
    private String name;

    // Construtor vazio (necessário para o Retrofit)
    public ExerciseSession() {}

    // Getter para pegarmos o nome do exercício na MainActivity
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
