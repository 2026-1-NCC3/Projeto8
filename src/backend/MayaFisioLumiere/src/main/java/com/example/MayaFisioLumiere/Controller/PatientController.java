package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.Patient.PatientRequestDTO;
import com.example.MayaFisioLumiere.Domain.Patient.PatientResponseDTO;
import com.example.MayaFisioLumiere.Services.PatientService;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Repository.PatientRepository; // importando para poder usar o auto-wired
import org.springframework.beans.factory.annotation.Autowired; // necessário para salvar as coisas dentro do banco de dados
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController //tipo da classe que vamos fazer
@RequestMapping("/api/patient")
//rota principal desse controller, sempre que formos adicionar uma rota nesse controle vai ser esse caminho +/nomeDaRota
public class PatientController {

    @Autowired
    private PatientRepository patientRepository; // conecta o controller ao banco de dados hospedado

    @Autowired
    private PatientService patientService;

    @GetMapping("/getAllPatients")
    public ResponseEntity<?> getAllPatients() {
        try {
            // busca todos os pacientes dentro do banco de dados
            List<PatientResponseDTO> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar pacientes.");
        }
    }

    @PostMapping("/createPatient")
    public ResponseEntity<?> createPatient(@RequestBody PatientRequestDTO body) {
        try {
            PatientEntity newPatient = this.patientService.createPatient(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar paciente: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody PatientRequestDTO body) {
        try {
            var response = patientService.loginPatient(body.email(), body.password());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/updatePatient/{id}")
    public ResponseEntity<?> updatePatientData(@PathVariable UUID id, @RequestBody PatientRequestDTO body) {
        try {
            PatientResponseDTO response = patientService.updatePatientData(body, id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateLgpdStatus/{id}")
    public ResponseEntity<?> updateLgpdStatus(@PathVariable UUID id, @RequestParam boolean lgpdCheck){
        try{
        patientService.updateLgpdStatus(id, lgpdCheck);
        return ResponseEntity.ok().body("Status LGPD atualizado com sucesso.");
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());        }

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        try {
            PatientResponseDTO patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<?> getPatientByName(@PathVariable String name, String surname) {
        try {

            List<PatientResponseDTO> patients = patientService.getPatientByFullName(name, surname);
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro na busca.");
        }
    }

    @DeleteMapping("/delete/{patient_id}") // deletando paciente por id
    public ResponseEntity<?> deletePatient(@PathVariable("patient_id") UUID patient_id) {
        try {
            patientRepository.deleteById(patient_id);
            return ResponseEntity.ok("Paciente deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar paciente.");
        }
    }

}