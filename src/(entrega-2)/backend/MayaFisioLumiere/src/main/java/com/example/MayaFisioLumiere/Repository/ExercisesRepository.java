package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExercisesRepository extends JpaRepository<ExerciseEntity, Long> {

    Optional<ExerciseEntity> findByTitle(String title);

    List<ExerciseEntity> findByTitleContainingIgnoreCase(String title);

    List<ExerciseEntity> findByTagsContainingIgnoreCase(String tag);

}
