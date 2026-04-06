package com.example.MayaFisioLumiere.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {

    List<PatientEntity> findByStatus(String status);

    List<PatientEntity> findByNameAndSurnameIgnoreCase(String name, String surname);

    boolean existsByNameAndSurname(String name, String surname);

    @Transactional
    void deleteByNameAndSurname(String name, String surname);


    Optional<PatientEntity> findByEmail(String email);
}