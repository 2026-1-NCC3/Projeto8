package com.example.projeto8.model;

public class Task {
    private String name;
    // ESTE É O CONSTRUTOR QUE ESTÁ FALTANDO:
    // Ele permite que você faça "new Task('Nome do Exercício')"
    public boolean isExpanded = false; // Necessário para o clique de expandir
    public boolean isDone = false;     // Necessário para o check verde


    public Task(String name) {
        this.name = name;
    }

    // Getter para o Adapter conseguir ler o nome e mostrar na tela
    public String getName() {
        return name;
    }

}
