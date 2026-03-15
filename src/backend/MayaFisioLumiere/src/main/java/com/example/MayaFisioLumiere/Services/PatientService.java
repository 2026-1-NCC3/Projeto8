package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.Patient.PatientRequestDTO;
import com.example.MayaFisioLumiere.Domain.Patient.PatientResponseDTO;
import com.example.MayaFisioLumiere.entity.PatientEntity;
import com.example.MayaFisioLumiere.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    //busca todos os pacientes criados dentro do banco de dados
    public List<PatientResponseDTO> getAllPatients() {
        List<PatientEntity> patients = this.patientRepository.findAll();

        return patients.stream()
                .map(patient -> new PatientResponseDTO(
                        patient.getPatient_ID(),
                        patient.getPassword(),
                        patient.getStatus(),
                        patient.getName(),
                        patient.getSurname(),
                        patient.getEmail()
                ))
                .toList();
    }

    // busca paciente por nome completo
    //CADE A ROTA?
    public List<PatientResponseDTO> getPatientByFullName(String name, String surname){
        List<PatientEntity> patients = this.patientRepository.findByNameAndSurnameIgnoreCase(name, surname);

        return patients.stream().map(patient -> new PatientResponseDTO(
                patient.getPatient_ID(),
                patient.getPassword(),
                patient.getStatus(),
                patient.getName(),
                patient.getSurname(),
                patient.getEmail()
        )).toList();
    }
    //cria novo paciente dentro do banco de dados
    public PatientEntity createPatient(PatientRequestDTO data){
        PatientEntity newPatient = new PatientEntity();
        newPatient.setName(data.name());
        newPatient.setSurname(data.surname());
        newPatient.setCpf(data.cpf());
        newPatient.setEmail(data.email());
        newPatient.setPassword(data.password());
        newPatient.setPatientAge(data.patientAge());

        return patientRepository.save(newPatient);
    }

    //deleta paciente pela uuid dele
    public void deletePatientById(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Paciente com ID " + id + " não encontrado.");
        }
        patientRepository.deleteById(id);
    }
}