package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findByPatient(UUID patientId);


    List<AppointmentEntity> findByDate(LocalDateTime date);
}
