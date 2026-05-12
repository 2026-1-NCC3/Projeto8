package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    @Query("SELECT a FROM AppointmentEntity a WHERE a.patient.patient_ID = :patientId")
    List<AppointmentEntity> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.patient.id = :patientId " +
            "AND EXTRACT(MONTH FROM a.date) = :month " +
            "AND EXTRACT(YEAR FROM a.date) = :year")
    List<AppointmentEntity> findByPatientAndMonth(@Param("patientId") UUID patientId,
                                                  @Param("month") int month,
                                                  @Param("year") int year);

    List<AppointmentEntity> findByDate(LocalDateTime date);
}
