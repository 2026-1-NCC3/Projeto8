package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.WorkoutSession.WorkoutSesRequestDTO;
import com.example.MayaFisioLumiere.Domain.WorkoutSession.WorkoutSesResponseDTO;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Entity.WorkoutSessionEntity;
import com.example.MayaFisioLumiere.Services.WorkoutSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/workout")
public class WorkoutSessionController {

    @Autowired
    private WorkoutSessionService workoutSessionService;

    // Criar o workout quando cria a ExerciseSession
    // /workout/create-workout
    @PostMapping("/create-workout")
    public WorkoutSessionEntity createWorkout(@RequestBody WorkoutSesRequestDTO data) {
        return workoutSessionService.createWorkout(data);
    }

    // Paciente dar check no workout do dia
    // /workout/check/2
    @PutMapping("/check/{id}")
    public ResponseEntity<?> checkWorkout(@PathVariable Long id) {
        try {
            WorkoutSessionEntity workout = workoutSessionService.checkWorkout(id);
            return ResponseEntity.ok(workout);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao marcar treino como concluído");
        }
    }

        //Resetar o check após 6 dias para o paciente treinar a próxima semana
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void resetOldWorkouts() {
        LocalDate limitDate = LocalDate.now().minusDays(6);

        List<WorkoutSessionEntity> oldWorkouts = workoutSessionRepository
                .findByCheckedTrueAndWorkoutDateBefore(limitDate);

        for (WorkoutSessionEntity workout : oldWorkouts) {
            workout.setChecked(false);
            workout.setWorkoutDate(null); // Limpa a data para o próximo check

            //Para resetar a dor do exercício
            if (workout.getExerciseSessions() != null) {
                workout.getExerciseSessions().forEach(session -> session.setFeelPain(false));
            }
        }
        workoutSessionRepository.saveAll(oldWorkouts);
    }

        // Verificar progresso desta semana
    public Map<String, Object> getWeeklyProgress(UUID patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        List<WorkoutSessionEntity> allWorkouts = workoutSessionRepository.findByPatient(patient);
        long total = allWorkouts.size();

        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<WorkoutSessionEntity> completedWorkouts
                = workoutSessionRepository.findByPatientAndWorkoutDateAfterAndCheckedTrue(patient, sevenDaysAgo);
        long completed = completedWorkouts.size();

        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("completed", completed);

        return response;
    }

    // Buscar todos os pacientes ativos
    // /workout/patients/active
    @GetMapping("/patients/active")
    public ResponseEntity<List<PatientEntity>> getActivePatients() {
        return ResponseEntity.ok(workoutSessionService.getActivePatients());
    }

    // Buscar todos os apcientes com status inativo
    // /workout/patients/inactive
    @GetMapping("/patients/inactive")
    public ResponseEntity<List<PatientEntity>> getInactivePatients() {
        return ResponseEntity.ok(workoutSessionService.getInactivePatients());
    }

    // Buscar todas as workout sessions de um paciente (patient id)
    // /workout/patient/3e8e4187-47d8-4751-955d-e6a036db9478
    @GetMapping("/patient/{patient_id}")
    public ResponseEntity<List<WorkoutSesResponseDTO>> getWorkoutsByPatient(@PathVariable UUID patient_id) {
        return ResponseEntity.ok(workoutSessionService.getWorkoutsByPatient(patient_id));
    }

    // Buscar todas as workout sessions de todos os pacientes
    // /workout/all
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutSessionEntity>> getAllWorkouts() {
        return ResponseEntity.ok(workoutSessionService.getAllWorkouts());
    }

    // Buscar p progresso semanal do paciente
    // /workout/progress/3e8e4187-47d8-4751-955d-e6a036db9478
    @GetMapping("/progress/{patient_id}")
    public ResponseEntity<Map<String, Object>> getWorkoutProgress(@PathVariable UUID patient_id) {
        return ResponseEntity.ok(workoutSessionService.getWeeklyProgress(patient_id));
    }

    // Deletar a workout Session de um paciente
    // /workout/deleteById/2
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteWorkoutSession(@PathVariable Long id) {
        workoutSessionService.deleteWorkoutSession(id);
        return ResponseEntity.ok("Workout deletado com sucesso");
    }
}
