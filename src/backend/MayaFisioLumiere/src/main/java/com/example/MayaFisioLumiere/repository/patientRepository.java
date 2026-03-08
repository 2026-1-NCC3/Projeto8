package com.example.MayaFisioLumiere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MayaFisioLumiere.entity.patientEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface patientRepository extends JpaRepository<patientEntity, UUID> {

    void deleteByEmail(String email);

    List<patientEntity> findByName(String name);

}