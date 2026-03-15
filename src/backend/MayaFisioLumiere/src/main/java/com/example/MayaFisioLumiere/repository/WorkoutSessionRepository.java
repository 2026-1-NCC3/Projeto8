package com.example.MayaFisioLumiere.repository;

import com.example.MayaFisioLumiere.entity.ExerciseEntity;
import com.example.MayaFisioLumiere.entity.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {

    List<WorkoutSessionEntity> findByPatient(Long id);
    List<ExerciseEntity> findByWeekDay(String title);


}
