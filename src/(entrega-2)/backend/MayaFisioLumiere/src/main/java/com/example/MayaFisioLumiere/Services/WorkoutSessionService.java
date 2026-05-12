package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;
import com.example.MayaFisioLumiere.Domain.WorkoutSession.WorkoutSesRequestDTO;
import com.example.MayaFisioLumiere.Domain.WorkoutSession.WorkoutSesResponseDTO;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Entity.WorkoutSessionEntity;
import com.example.MayaFisioLumiere.Repository.PatientRepository;
import com.example.MayaFisioLumiere.Repository.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkoutSessionService {

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private PatientRepository patientRepository;

    /* @Autowired
    private ExerciseSessionRepository exerciseSessionRepository;

    @Autowired
    private ExercisesRepository exercisesRepository; */

    // Criar um novo workout pegando o ID dos exercicios
    public WorkoutSessionEntity createWorkout(WorkoutSesRequestDTO data) {

        PatientEntity patient = patientRepository.findById(data.patient_id())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        WorkoutSessionEntity workout = new WorkoutSessionEntity();

        workout.setPatient(patient);
        workout.setWeekDay(data.weekDay());
        workout.setWorkoutDate(null);
        workout.setChecked(false);

        return workoutSessionRepository.save(workout);
    }

    // Permitir ao paciente dar check no Workout do dia (checked = true)
    @Transactional
    public WorkoutSessionEntity checkWorkout(Long workoutSession_id) {
        WorkoutSessionEntity workout = workoutSessionRepository.findById(workoutSession_id)
                .orElseThrow(() -> new RuntimeException("Workout não encontrado"));

        workout.setChecked(true);
        workout.setWorkoutDate(LocalDate.now());
        WorkoutSessionEntity savedWorkout = workoutSessionRepository.save(workout);
        checkPatientStatus(workout.getPatient().getPatient_ID());

            return savedWorkout;
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

    // Verificar se o paciente está ativo ou inativo
    public String checkPatientStatus(UUID patientId) {

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        List<WorkoutSessionEntity> workouts
                = workoutSessionRepository
                        .findByPatientAndWorkoutDateAfterAndCheckedTrue(patient, sevenDaysAgo);

        if (workouts.size() >= 2) {
            patient.setStatus("ATIVO");
        } else {
            patient.setStatus("INATIVO");
        }
        patientRepository.save(patient);
        return patient.getStatus();
    }

    // Buscar todos os pacientes com status ativo
    public List<PatientEntity> getActivePatients() {
        return patientRepository.findByStatus("ATIVO");
    }

    // Buscar todos os pacientes com status inativo
    public List<PatientEntity> getInactivePatients() {
        return patientRepository.findByStatus("INATIVO");
    }

    // Buscar todas as workout sessions de um paciente (patient id)
    /* public List<WorkoutSessionEntity> getWorkoutsByPatient(UUID patientId){

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        return workoutSessionRepository.findByPatient(patient);
    }
     */
    
    public List<WorkoutSesResponseDTO> getWorkoutsByPatient(UUID patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        List<WorkoutSessionEntity> workouts = workoutSessionRepository.findByPatient(patient);

        return workouts.stream().map(entity -> new WorkoutSesResponseDTO(
                entity.getWorkoutSession_id(),
                entity.getWeekDay(),
                entity.getChecked(),
                entity.getPatient().getPatient_ID(),
                entity.getExerciseSessions().stream().map(ex -> new ExerciseSessionResponseDTO(
                Math.toIntExact(ex.getExercisesession_id()),
                new ExerciseResponseDTO(
                        ex.getExercise().getExercise_ID(),
                        ex.getExercise().getTitle(),
                        ex.getExercise().getMidiaURL(),
                        ex.getExercise().getTags(),
                        ex.getExercise().getDescription()
                ),
                ex.getWorkoutSession().getWorkoutSession_id(),
                ex.getPatient().getPatient_ID(),
                ex.getSerie(),
                ex.getRepetitions(),
                ex.getFeelPain()
        )).toList()
        )).toList();
    }

    // Buscar todas as Workout Sessions de todos os pacientes
    public List<WorkoutSessionEntity> getAllWorkouts() {
        return workoutSessionRepository.findAll();
    }

    // Deletar a Workout Session de um paciente
    public void deleteWorkoutSession(Long workoutSessionId) {

        WorkoutSessionEntity workout = workoutSessionRepository.findById(workoutSessionId)
                .orElseThrow(() -> new RuntimeException("Workout não encontrado"));

        workoutSessionRepository.delete(workout);
    }
}
