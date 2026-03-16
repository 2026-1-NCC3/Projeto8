package com.example.MayaFisioLumiere.repository;

import com.example.MayaFisioLumiere.Domain.ExerciseSession.ExerciseSessionResponseDTO;
import com.example.MayaFisioLumiere.entity.ExerciseSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExerciseSessionRepository extends JpaRepository<ExerciseSessionEntity, Long> {

}
