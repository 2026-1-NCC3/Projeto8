package com.example.MayaFisioLumiere.Repository;

import com.example.MayaFisioLumiere.Entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long>{

    Optional<AdminEntity> findByAdminEmail(String adminEmail);
    List<AdminEntity> findByAdminEmailContainingIgnoreCase(String adminEmail);
}