package com.example.MayaFisioLumiere.repository;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.entity.ExerciseSessionEntity;
import com.example.MayaFisioLumiere.entity.PatientEntity;
import com.example.MayaFisioLumiere.entity.WorkoutSessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExerciseSessionRepository extends JpaRepository<ExerciseSessionEntity, Long> {

    List<ExerciseSessionResponseDTO> getAllExerciseSessions();
    @Transactional
    void deleteExerciseSession(Long exercisesSession_id);
}
