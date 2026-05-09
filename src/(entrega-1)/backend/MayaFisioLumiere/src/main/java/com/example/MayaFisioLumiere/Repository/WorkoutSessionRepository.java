package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Entity.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {

   // List<WorkoutSessionEntity> findByWeekDay(String weekDay);
   // List<WorkoutSessionEntity> findByChecked(Boolean checked);

    List<WorkoutSessionEntity> findByPatientAndWorkoutDateAfterAndCheckedTrue(
            PatientEntity patient,
            LocalDate date
    );


    List<WorkoutSessionEntity> findByPatient(PatientEntity patient);

}
