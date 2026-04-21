package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionRequestDTO;
import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;
import com.example.MayaFisioLumiere.Entity.ExerciseEntity;
import com.example.MayaFisioLumiere.Entity.ExerciseSessionEntity;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Entity.WorkoutSessionEntity;
import com.example.MayaFisioLumiere.Repository.ExerciseSessionRepository;
import com.example.MayaFisioLumiere.Repository.ExercisesRepository;
import com.example.MayaFisioLumiere.Repository.PatientRepository;
import com.example.MayaFisioLumiere.Repository.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ExerciseSessionService {

    @Autowired
    private ExerciseSessionRepository exerciseSessionRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ExercisesRepository exercisesRepository;
    @Autowired
    private  WorkoutSessionRepository workoutSessionRepository;

    public List<ExerciseSessionResponseDTO> getAllExerciseSessions() {
        List<ExerciseSessionEntity> sessions = exerciseSessionRepository.findAll();

        return sessions.stream().map(entity -> new ExerciseSessionResponseDTO(
                        Math.toIntExact(entity.getExercisesession_id()),
                        new ExerciseResponseDTO( // Criando o DTO do exercício com os dados da Entity, para retornar os dados deles
                                entity.getExercise().getExercise_ID(),
                                entity.getExercise().getTitle(),
                                entity.getExercise().getMidiaURL(),
                                entity.getExercise().getTags(),
                                entity.getExercise().getDescription()
                        ),
                        entity.getWorkoutSession().getWorkoutSession_id(),
                        entity.getPatient().getPatient_ID(),
                        entity.getSerie(),
                        entity.getRepetitions(),
                        entity.getFeelPain()
                )
        ).toList();
    }


    public ExerciseSessionEntity createExerciseSession(ExerciseSessionRequestDTO data){
        ExerciseSessionEntity newexerciseSession = new ExerciseSessionEntity();

        PatientEntity patient = patientRepository.findById(data.patient_id()) //buscando paciente pelo id
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        newexerciseSession.setPatient(patient);

        ExerciseEntity exercise = exercisesRepository.findById(data.exercise_id()) //buscando exercício pelo id
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        newexerciseSession.setExercise(exercise);

        WorkoutSessionEntity workoutSession = workoutSessionRepository.findById(data.workoutSession())
                .orElseThrow(() -> new RuntimeException("Treino não encontrado"));

        newexerciseSession.setWorkoutSession(workoutSession);
        newexerciseSession.setSerie(data.serie());
        newexerciseSession.setRepetitions(data.repetitions());
        newexerciseSession.setFeelPain(data.feelPain());

        return exerciseSessionRepository.save(newexerciseSession);
    }
    //atualizar a sessão de exercicios
    public ExerciseSessionEntity updateExerciseSession(Long exercisesession_id, ExerciseSessionRequestDTO data) {
        ExerciseSessionEntity session = exerciseSessionRepository.findById(exercisesession_id)
                .orElseThrow(() -> new RuntimeException("Sessão de exercício não encontrada"));

        if (data.serie() != null) {
            session.setSerie(data.serie());
        }
        if (data.repetitions() != null) {
            session.setRepetitions(data.repetitions());
        }

        if (data.patient_id() != null) {
            PatientEntity patient = patientRepository.findById(data.patient_id())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            session.setPatient(patient);
        }

        if (data.exercise_id() != null) {
            ExerciseEntity exercise = exercisesRepository.findById(data.exercise_id())
                    .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));
            session.setExercise(exercise);
        }

        if (data.workoutSession() != null) {
            WorkoutSessionEntity workoutSession = workoutSessionRepository.findById(data.workoutSession())
                    .orElseThrow(() -> new RuntimeException("Treino não encontrado"));
            session.setWorkoutSession(workoutSession);
        }
        return exerciseSessionRepository.save(session);
    }

    //Atualizar o feelPain da exerciseSession na visão do Patient quando ele terminar o exercício

    public ExerciseSessionEntity updateExerciseSessionPain(UUID patientId, Long exerciseSession_id, ExerciseSessionRequestDTO data) {
        // 1. Busca a sessão e já verifica se ela existe
        ExerciseSessionEntity session = exerciseSessionRepository.findById(exerciseSession_id)
                .orElseThrow(() -> new RuntimeException("Sessão de exercício não encontrada"));

        if (!session.getPatient().getPatient_ID().equals(patientId)) {
            throw new RuntimeException("Esta sessão não pertence ao paciente informado");
        }

        if (data.feelPain() != null) {
            session.setFeelPain(data.feelPain());
        }

        return exerciseSessionRepository.save(session);
    }

    @Transactional
    public void deleteExerciseSession(Long exercisesession_id) {
        ExerciseSessionEntity session = exerciseSessionRepository.findById(exercisesession_id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

        WorkoutSessionEntity workout = session.getWorkoutSession();
        if (workout != null) {
            workout.getExerciseSessions().remove(session);
        }
        exerciseSessionRepository.delete(session);
    }
}