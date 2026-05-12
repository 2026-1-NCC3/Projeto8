package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.Patient.PatientRequestDTO;
import com.example.MayaFisioLumiere.Domain.Patient.PatientResponseDTO;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Entity.role.UserRole;
import com.example.MayaFisioLumiere.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    // Busca todos os pacientes criados dentro do banco de dados
    public List<PatientResponseDTO> getAllPatients() {
        List<PatientEntity> patients = this.patientRepository.findAll();

        return patients.stream()
                .map(patient -> new PatientResponseDTO(
                patient.getPatient_ID(),
                patient.getPassword(),
                patient.getStatus(),
                patient.getName(),
                patient.getSurname(),
                patient.getEmail(),
                patient.getBirthDate(),
                patient.getCellPhone(),
                patient.getGender(),
                patient.getHeight(),
                patient.getWeight(),
                patient.isLgpdCheck(),
                patient.getDescription(),
                patient.getCpf()
        ))
                .toList();
    }

    // Busca paciente por ID (UUID)
    public PatientResponseDTO getPatientById(UUID id) {
        PatientEntity patient = this.patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com o ID: " + id));

        return new PatientResponseDTO(
                patient.getPatient_ID(),
                patient.getPassword(),
                patient.getStatus(),
                patient.getName(),
                patient.getSurname(),
                patient.getEmail(),
                patient.getBirthDate(),
                patient.getCellPhone(),
                patient.getGender(),
                patient.getHeight(),
                patient.getWeight(),
                patient.isLgpdCheck(),
                patient.getDescription(),
                patient.getCpf()
        );
    }

    // Busca paciente por nome completo
    public List<PatientResponseDTO> getPatientByFullName(String name, String surname) {
        List<PatientEntity> patients = this.patientRepository.findByNameAndSurnameIgnoreCase(name, surname);

        return patients.stream().map(patient -> new PatientResponseDTO(
                patient.getPatient_ID(),
                patient.getPassword(),
                patient.getStatus(),
                patient.getName(),
                patient.getSurname(),
                patient.getEmail(),
                patient.getBirthDate(),
                patient.getCellPhone(),
                patient.getGender(),
                patient.getHeight(),
                patient.getWeight(),
                patient.isLgpdCheck(),
                patient.getDescription(),
                patient.getCpf()
        )).toList();
    }

    // Cria novo paciente dentro do banco de dados
    public PatientEntity createPatient(PatientRequestDTO data) {
        PatientEntity newPatient = new PatientEntity();
        newPatient.setName(data.name());
        newPatient.setSurname(data.surname());
        newPatient.setCpf(data.cpf());
        newPatient.setEmail(data.email());
        newPatient.setPassword(data.password());
        newPatient.setBirthDate(data.birthDate());
        newPatient.setCellPhone(data.cellPhone());
        newPatient.setGender(data.gender());
        newPatient.setHeight(data.height());
        newPatient.setWeight(data.weight());
        newPatient.setPatientAge(data.patientAge()); //remover? tirar?
        newPatient.setRole(UserRole.PATIENT);
        newPatient.setTotalMinutesUsedToday(0);
        newPatient.setLastAccessDate(java.time.LocalDate.now());
        newPatient.setPassword(data.birthDate());
        newPatient.setDescription(data.description());

        return patientRepository.save(newPatient);
    }

    public Map<String, Object> loginPatient(String email, String password) {
        PatientEntity patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        if (!patient.getPassword().equals(password)) {
            throw new RuntimeException("A senha está incorreta");
        }

        String token = tokenService.generateToken(patient);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", patient.getEmail());
        response.put("patient_id", patient.getPatient_ID());
        response.put("name", patient.getName());
        response.put("lgpd_check", patient.isLgpdCheck());

        return response;
    }

    // Atualiza dados do paciente
    public PatientResponseDTO updatePatientData(PatientRequestDTO body, UUID id) {
        PatientEntity patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        if (body.name() != null) {
            patient.setName(body.name());
        }
        if (body.surname() != null) {
            patient.setSurname(body.surname());
        }
        if (body.cpf() != null) {
            patient.setCpf(body.cpf());
        }
        if (body.email() != null) {
            patient.setEmail(body.email());
        }
        if (body.password() != null) {
            patient.setPassword(body.password());
        }
        if (body.patientAge() != null) {
            patient.setPatientAge(body.patientAge());
        }
        if (body.birthDate() != null) {
            patient.setBirthDate(body.birthDate());
        }
        if (body.cellPhone() != null) {
            patient.setCellPhone(body.cellPhone());
        }
        if (body.gender() != null) {
            patient.setGender(body.gender());
        }
        if (body.height() != null) {
            patient.setHeight(body.height());
        }
        if (body.weight() != null) {
            patient.setWeight(body.weight());
        }
        if (body.description() != null) {
            patient.setDescription(body.description());
        }

        patientRepository.save(patient);

        return new PatientResponseDTO(
                patient.getPatient_ID(),
                patient.getPassword(),
                patient.getStatus(),
                patient.getName(),
                patient.getSurname(),
                patient.getEmail(),
                patient.getBirthDate(),
                patient.getCellPhone(),
                patient.getGender(),
                patient.getHeight(),
                patient.getWeight(),
                patient.isLgpdCheck(),
                patient.getDescription(),
                patient.getCpf()
        );
    }

    // Atualiza o status lgpd do paciente
    public void updateLgpdStatus(UUID id, boolean lgpdCheck) {
        PatientEntity patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        patient.setLgpdCheck(lgpdCheck);
        patientRepository.save(patient);
    }

    // Deleta paciente pela uuid dele
    public void deletePatientById(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Paciente com ID " + id + " não encontrado.");
        }
        patientRepository.deleteById(id);
    }
}
