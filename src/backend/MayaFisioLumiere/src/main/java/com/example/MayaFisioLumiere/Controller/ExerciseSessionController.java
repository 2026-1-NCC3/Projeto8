package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionRequestDTO;
import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.Services.ExerciseService;
import com.example.MayaFisioLumiere.Services.ExerciseSessionService;
import com.example.MayaFisioLumiere.Entity.ExerciseSessionEntity;
import com.example.MayaFisioLumiere.Repository.ExerciseSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exerciseSession")
public class ExerciseSessionController {
    @Autowired
    private ExerciseSessionRepository exerciseSessionRepository;
    private ExerciseSessionEntity exerciseSessionEntity;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private ExerciseSessionService exerciseSessionService;

    @GetMapping("/getAllExerciseSessions")
    public ResponseEntity<?> getAllExerciseSessions() {
        try {
            List<ExerciseSessionResponseDTO> allExerciseSession = exerciseSessionService.getAllExerciseSessions();
            return ResponseEntity.ok(allExerciseSession);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao encontrar as sessões de exercício");
        }

    }

    @PostMapping("/createExerciseSession")
    public ResponseEntity<?> createExerciseSession(@RequestBody ExerciseSessionRequestDTO body) {
        try {
            ExerciseSessionEntity newExerciseSession = this.exerciseSessionService.createExerciseSession(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(newExerciseSession);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }


    @PutMapping("/updateExerciseSession/{id}")
    public ResponseEntity<?> updateExerciseSession(
            @PathVariable Long id,
            @RequestBody ExerciseSessionRequestDTO data) {
        try {
            ExerciseSessionEntity updatedSession = exerciseSessionService.updateExerciseSession(id, data);
            return ResponseEntity.ok(updatedSession); //retorna ok com sessão atualizada
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a atualização: " + e.getMessage());
        }
    }

    //Atualizar o feelPain do ExerciseSession updateExerciseSessionPain
    //Rota
    @PutMapping("/updateExerciseSessionPain/{patient_id}")
    public ResponseEntity<?> updateExerciseSessionPain(
            @PathVariable UUID patient_id,
            @PathVariable Long exerciseSession_id, // so deu certo quando se busca pela sessão pelo id dela
            @RequestBody ExerciseSessionRequestDTO data) {
        try {
            ExerciseSessionEntity updatedSession = exerciseSessionService.updateExerciseSessionPain(patient_id, exerciseSession_id, data);
            return ResponseEntity.ok(updatedSession);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }


    @DeleteMapping("/deleteExerciseSession/{exercisesession_id}")
    public ResponseEntity<?> deleteExerciseSession(@PathVariable Long exercisesession_id) {
        try {
            exerciseSessionService.deleteExerciseSession(exercisesession_id);
            return ResponseEntity.ok("Sessão deletada com sucesso");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar");
        }
    }
}
