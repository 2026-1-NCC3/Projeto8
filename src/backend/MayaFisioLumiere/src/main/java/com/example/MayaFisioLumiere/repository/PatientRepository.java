package com.example.MayaFisioLumiere.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MayaFisioLumiere.entity.PatientEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {

    List<PatientEntity> findByNameAndSurnameIgnoreCase(String name, String surname);

    boolean existsByNameAndSurname(String name, String surname);

    @Transactional
    void deleteByNameAndSurname(String name, String surname);
}