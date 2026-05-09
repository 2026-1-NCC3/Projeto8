package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Repository.AdminRepository;
import com.example.MayaFisioLumiere.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar o email na tabela de Administradores
        var admin = adminRepository.findByAdminEmail(username);
        if (admin.isPresent()) {
            return admin.get();
        }

        // Tenta buscar na tabela de Pacientes
        var patient = patientRepository.findByEmail(username);
        if (patient.isPresent()) {
            return patient.get();
        }

        // Se não achar em nenhum dos dois, o login falha
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
