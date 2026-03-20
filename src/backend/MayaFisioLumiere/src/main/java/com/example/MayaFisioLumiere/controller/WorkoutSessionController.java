package com.example.MayaFisioLumiere.controller;


import com.example.MayaFisioLumiere.Domain.WorkoutSession.WorkoutSesRequestDTO;
import com.example.MayaFisioLumiere.entity.PatientEntity;
import com.example.MayaFisioLumiere.entity.WorkoutSessionEntity;
import com.example.MayaFisioLumiere.Services.WorkoutSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workout")
public class WorkoutSessionController {

    @Autowired
    private WorkoutSessionService workoutSessionService;

    //Criar o workout quando cria a ExerciseSession
    //ROTA http://localhost:8081/workout/create-workout
    @PostMapping("/create-workout")
    public WorkoutSessionEntity createWorkout(@RequestBody WorkoutSesRequestDTO data){
        return workoutSessionService.createWorkout(data);
    }

    //Paciente dar check no workout do dia
    //ROTA http://localhost:8081/workout/check/2
    @PutMapping("/check/{id}")
    public ResponseEntity<?> checkWorkout(@PathVariable Long id){
        try{
            WorkoutSessionEntity workout = workoutSessionService.checkWorkout(id);
            return ResponseEntity.ok(workout);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Erro ao marcar treino como concluído");
        }
    }

    //Buscar todos os pacientes ativos
    //ROTA http://localhost:8081/workout/patients/active
    @GetMapping("/patients/active")
    public ResponseEntity<List<PatientEntity>> getActivePatients(){
        return ResponseEntity.ok(workoutSessionService.getActivePatients());
    }

    //Buscar todos os apcientes com status inativo
    // ROTA http://localhost:8081/workout/patients/inactive
    @GetMapping("/patients/inactive")
    public ResponseEntity<List<PatientEntity>> getInactivePatients(){
        return ResponseEntity.ok(workoutSessionService.getInactivePatients());
    }

    //Buscar todas as workout sessions de um paciente (patient id)
    //ROTA http://localhost:8081/workout/patient/3e8e4187-47d8-4751-955d-e6a036db9478
    @GetMapping("/patient/{patient_id}")
    public ResponseEntity<List<WorkoutSessionEntity>> getWorkoutsByPatient(@PathVariable UUID patient_id){
        return ResponseEntity.ok(workoutSessionService.getWorkoutsByPatient(patient_id));
    }

    //Buscar todas as workout sessions de todos os pacientes
    //ROTA http://localhost:8081/workout/all
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutSessionEntity>> getAllWorkouts(){
        return ResponseEntity.ok(workoutSessionService.getAllWorkouts());
    }

    //Deletar a workout Session de um paciente
    //ROTA http://localhost:8081/workout/deleteById/2
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteWorkoutSession(@PathVariable Long id){
        workoutSessionService.deleteWorkoutSession(id);
        return ResponseEntity.ok("Workout deletado com sucesso");
    }
}