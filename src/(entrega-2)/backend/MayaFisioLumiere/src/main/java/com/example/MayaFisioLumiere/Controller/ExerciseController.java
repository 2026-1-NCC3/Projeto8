package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseRequestDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;
import com.example.MayaFisioLumiere.Entity.ExerciseEntity;
import com.example.MayaFisioLumiere.Services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    // Criar exercícios novos
    // /api/exercise/create-exercise
    @PostMapping("/create-exercise")
    public ResponseEntity<ExerciseEntity> create(@RequestBody ExerciseRequestDTO body) {
        ExerciseEntity newExercise = this.exerciseService.createExercise(body);
        return ResponseEntity.ok(newExercise);
    }

    // Atualizar o exercício
    // /api/exercise/updateExercise/8
    @PutMapping("/updateExercise/{id}")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(
            @PathVariable Long id,
            @RequestBody ExerciseRequestDTO data) {
        ExerciseResponseDTO exercise = exerciseService.updateExercise(id, data);
        return ResponseEntity.ok(exercise);
    }

    // Retornar os exercícios cadastrados, com paginação, para não retornar TODOS os
    // exercícios e uma vez, e sim alguns por página e depois scrollar ou clicar no +1 página
    // /api/exercise/all?page=2&size=3,
    @GetMapping("/all")
    public ResponseEntity<List<ExerciseResponseDTO>> getExercises(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ExerciseResponseDTO> allExercises = this.exerciseService.getAllExercises(page, size);
        return ResponseEntity.ok(allExercises);
    }

    // Buscar por título exato
    // /api/exercise/searchByTitle/flexão
    @GetMapping("/searchByTitle/{title}")
    public ResponseEntity<List<ExerciseResponseDTO>> searchByTitle(
            @PathVariable String title) {

        List<ExerciseResponseDTO> exercises = exerciseService.findByTitleContaining(title);
        return ResponseEntity.ok(exercises);
    }

    // Procurar exercício por id
    // /api/exercise/5
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(
            @PathVariable Long id) {

        ExerciseResponseDTO exercise = exerciseService.findById(id);

        return ResponseEntity.ok(exercise);
    }

    // Buscar exercício por tag
    // /api/exercise/tag/superiores
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<ExerciseResponseDTO>> searchByTag(
            @PathVariable String tag) {

        List<ExerciseResponseDTO> exercises = exerciseService.findByTags(tag);
        return ResponseEntity.ok(exercises);
    }

    // Deletar exercício por id
    // /api/exercise/deleteExerciseId/5
    @DeleteMapping("/deleteExerciseId/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable Long id) {

        exerciseService.deleteExerciseById(id);
        return ResponseEntity.ok("Exercicio deletado com sucesso");
    }
}
