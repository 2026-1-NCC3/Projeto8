package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionRequestDTO;
import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.Services.ExerciseService;
import com.example.MayaFisioLumiere.Services.ExerciseSessionService;
import com.example.MayaFisioLumiere.Entity.ExerciseSessionEntity;
import com.example.MayaFisioLumiere.Repository.ExerciseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exerciseSession")
public class ExerciseSessionController {
    @Autowired
    private ExerciseSessionRepository exerciseSessionRepository;
    private ExerciseSessionEntity exerciseSessionEntity;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private ExerciseSessionService exerciseSessionService;

    @GetMapping("/getAllExerciseSessions")
    public ResponseEntity<?> getAllExerciseSessions(){
       try{
        List<ExerciseSessionResponseDTO> allExerciseSession = exerciseSessionService.getAllExerciseSessions();
        return ResponseEntity.ok(allExerciseSession);
       }catch(Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao encontrar as sessões de exercício");
        }

    }
    @PostMapping("/createExerciseSession")
    public ResponseEntity<?> createExerciseSession(@RequestBody ExerciseSessionRequestDTO body){
        try{
            ExerciseSessionEntity newExerciseSession = this.exerciseSessionService.createExerciseSession(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(newExerciseSession);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar sessão de exercicios");
        }
    }
    @PutMapping("/updateExerciseSession")
    public ResponseEntity<?> updateExerciseSession( @PathVariable Long id,
                                                    @RequestBody ExerciseSessionRequestDTO data){
        try {
            ExerciseSessionEntity updatedSession = exerciseSessionService.updateExerciseSession(id, data);
            return ResponseEntity.ok(updatedSession); //retorna ok com sessão atualizada
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a atualização: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteExerciseSession")
    public  ResponseEntity<?> deleteExerciseSession(Long exerciseSession_id){
        try{
            exerciseSessionRepository.deleteById(exerciseSession_id);
            return ResponseEntity.ok("Sessão de exercícios deletada com sucesso");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar sessão de exercícios");
        }
    }
}
