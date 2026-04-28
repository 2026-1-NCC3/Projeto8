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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service

public class WorkoutSessionService {
    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private PatientRepository patientRepository;
/*
    @Autowired
    private ExerciseSessionRepository exerciseSessionRepository;

    @Autowired
    private ExercisesRepository exercisesRepository; */

    //Criar um novo workout pegando o id dos exercicios - PERMITIR associar mais de um exercise id

    public WorkoutSessionEntity createWorkout(WorkoutSesRequestDTO data){

        PatientEntity patient = patientRepository.findById(data.patient_id())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        WorkoutSessionEntity workout = new WorkoutSessionEntity();

        workout.setPatient(patient);
        workout.setWeekDay(data.weekDay());
        workout.setWorkoutDate(null);
        workout.setChecked(false);

        return workoutSessionRepository.save(workout);
    }

    //Permitir o paciente dar Check no Workout do dia, setando o checked como true. Associar essa logica ao botao no front

    public WorkoutSessionEntity checkWorkout(Long workoutSession_id){
        WorkoutSessionEntity workout = workoutSessionRepository.findById(workoutSession_id)
                .orElseThrow(() -> new RuntimeException("Workout não encontrado"));

        workout.setChecked(true);
        if (workout.getChecked() == true) {
                workout.setWorkoutDate(LocalDate.now());
        }
        //atualiza ststaus do paciente
        checkPatientStatus(workout.getPatient().getPatient_ID());
        return workoutSessionRepository.save(workout);
    }

    //Verificar se o paciente está ativo (deu checked em no minimo 2 workouts nos ultimos 7 dias)
    public String checkPatientStatus(UUID patientId){

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        List<WorkoutSessionEntity> workouts =
                workoutSessionRepository
                        .findByPatientAndWorkoutDateAfterAndCheckedTrue(patient, sevenDaysAgo);

        if(workouts.size() >= 2){
            patient.setStatus("ATIVO");
        } else {
            patient.setStatus("INATIVO");
        }
        patientRepository.save(patient);
        return patient.getStatus();
    }

    //Buscar todos os pacientes com status ativo
    public List<PatientEntity> getActivePatients(){
        return patientRepository.findByStatus("ATIVO");
    }

    //Buscar todos os pacientes com status inativo
    public List<PatientEntity> getInactivePatients(){
        return patientRepository.findByStatus("INATIVO");
    }

    //Buscar todas as workout sessions de um paciente (patient id)
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
                // Mapeando a lista de exercícios que está dentro do treino
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


    //Buscar todas as workout sessions de todos os pacientes
    public List<WorkoutSessionEntity> getAllWorkouts(){
        return workoutSessionRepository.findAll();
    }

    //Deletar a workout Session de um paciente
    public void deleteWorkoutSession(Long workoutSessionId){

        WorkoutSessionEntity workout = workoutSessionRepository.findById(workoutSessionId)
                .orElseThrow(() -> new RuntimeException("Workout não encontrado"));

        workoutSessionRepository.delete(workout);
    }



}
