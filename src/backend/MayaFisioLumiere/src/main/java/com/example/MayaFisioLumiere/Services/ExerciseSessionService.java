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

import java.time.LocalDate;
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
        List<ExerciseSessionEntity> sessions = exerciseSessionRepository.findAll();

        return sessions.stream().map(entity -> new ExerciseSessionResponseDTO(
                Math.toIntExact(entity.getExercisesession_id()),
                entity.getExercise().getExercise_ID(),
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

    public ExerciseSessionEntity updateExerciseSessionPain(Long exercisesession_id, ExerciseSessionRequestDTO data) {
        ExerciseSessionEntity session = exerciseSessionRepository.findById(exercisesession_id)
                .orElseThrow(() -> new RuntimeException("Sessão de exercício não encontrada"));

            if (data.feelPain() != null) {
                session.setFeelPain(data.feelPain());
            }
        return exerciseSessionRepository.save(session);
    }


    public void deleteExerciseSession(Long exercisession_id) {
         try {
            if(!exerciseSessionRepository.existsById(exercisession_id)){
                throw new RuntimeException("Sessão de Exercicios não encontrada");
            }
             exerciseSessionRepository.deleteById(exercisession_id);
         }catch (Exception err) {
            throw new RuntimeException("Erro ao deletar Sessão de Exercícios", err);
            }
}

}