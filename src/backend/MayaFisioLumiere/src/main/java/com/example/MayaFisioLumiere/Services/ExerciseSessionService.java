package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionRequestDTO;
import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.entity.ExerciseEntity;
import com.example.MayaFisioLumiere.entity.ExerciseSessionEntity;
import com.example.MayaFisioLumiere.entity.PatientEntity;
import com.example.MayaFisioLumiere.entity.WorkoutSessionEntity;
import com.example.MayaFisioLumiere.repository.ExerciseSessionRepository;
import com.example.MayaFisioLumiere.repository.ExercisesRepository;
import com.example.MayaFisioLumiere.repository.PatientRepository;
import com.example.MayaFisioLumiere.repository.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<ExerciseSessionEntity> sessions = this.exerciseSessionRepository.findAll();

        return sessions.stream()
                .map(entity -> new ExerciseSessionResponseDTO(
                        Math.toIntExact(entity.getExercisesession_id())
                ))
                .collect(Collectors.toList());
    }
    public ExerciseSessionEntity createExerciseSession(ExerciseSessionRequestDTO data){
        ExerciseSessionEntity newexerciseSession = new ExerciseSessionEntity();
        PatientEntity patient = patientRepository.findById(data.patient_id()) //buscando paciente pelo id
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        newexerciseSession.setPatient(patient);
        ExerciseEntity exercise = exercisesRepository.findById(data.exercise_id()) //buscando exercício pelo id
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));
        newexerciseSession.setExercise(exercise);
        WorkoutSessionEntity workoutSession = workoutSessionRepository.findById(data.workoutsession_id()) .orElseThrow(() -> new RuntimeException("Treino não encontrado"));
        newexerciseSession.setWorkoutSession_id(workoutSession);
        return exerciseSessionRepository.save(newexerciseSession);
    }
    //atualizar a sessão de exercicios
    public ExerciseSessionEntity updateExerciseSession(Long exercisesession_id, ExerciseSessionRequestDTO data) {
        ExerciseSessionEntity session = exerciseSessionRepository.findById(exercisesession_id)
                .orElseThrow(() -> new RuntimeException("Sessão de exercício não encontrada"));

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

        if (data.workoutsession_id() != null) {
            WorkoutSessionEntity workoutSession = workoutSessionRepository.findById(data.workoutsession_id())
                    .orElseThrow(() -> new RuntimeException("Treino não encontrado"));
            session.setWorkoutSession(workoutSession);
        } //quando criar certinho o workout session ver se ainda está dando erro aqui

        return exerciseSessionRepository.save(session);
    }

    public void deleteExerciseSession(Long exercisession_id) {
        ExerciseSessionEntity session = exerciseSessionRepository.findById( exercisession_id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada para exclusão"));

        exerciseSessionRepository.delete(session);
    }

}