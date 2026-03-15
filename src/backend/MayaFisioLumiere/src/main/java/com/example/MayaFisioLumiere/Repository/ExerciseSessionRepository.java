package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.Entity.ExerciseSessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExerciseSessionRepository extends JpaRepository<ExerciseSessionEntity, Long> {

    List<ExerciseSessionResponseDTO> getAllExerciseSessions();
    @Transactional
    void deleteExerciseSession(Long exercisesSession_id);
}
