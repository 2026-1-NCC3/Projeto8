package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Entity.ExerciseEntity;
import com.example.MayaFisioLumiere.Entity.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {

    List<WorkoutSessionEntity> findByPatient(Long id);
    List<ExerciseEntity> findByWeekDay(String title);


}
