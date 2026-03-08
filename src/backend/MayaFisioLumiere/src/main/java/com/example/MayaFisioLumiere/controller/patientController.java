package com.example.MayaFisioLumiere.controller;

import com.example.MayaFisioLumiere.entity.patientEntity;
import com.example.MayaFisioLumiere.repository.patientRepository; // importando para pdoer usar o auto-wired
import org.springframework.beans.factory.annotation.Autowired; // necessário para salvar as coisas dentro do banco de dados
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController //tipo da classe que vamos fazer
@RequestMapping("/api/patients")  //rota principal desse controller, sempre que formos adicionar uma rota nesse controle vai ser esse caminho +/nomeDaRota
public class patientController {

    @Autowired
    private patientRepository patientRepository; // conecta o controller ao banco de dados hospedado

    // classe para o paciente atualizar a senha dele
    public static class passwordUpdate {
        public String email;
        public String newPassword;
    }

    @GetMapping("/getAllPatients")
    public ResponseEntity<?> getAllPatients() {
        try {
            // busca todos os pacientes dentro do banco de dados
            List<patientEntity> patients = patientRepository.findAll();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar pacientes.");
        }
    }

    @PostMapping("/createPatient")
    public ResponseEntity<?> createPatient(@RequestBody patientEntity patient) {
        try {
            // cria o paciente dentro do banco de dados
            patientEntity savedPatient = patientRepository.save(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar paciente: verifique os dados.");
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<?> getPatientByName(@PathVariable String name) {
        try {
            // buscar paciente pelo nome
            List<patientEntity> patients = patientRepository.findByName(name);
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado.");
        }
    }

    @DeleteMapping("/delete/{email}") // deletando paciente por email
    public ResponseEntity<?> deletePatient(@PathVariable String email) {
        try {
            patientRepository.deleteByEmail(email);
            return ResponseEntity.ok("Paciente deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar paciente.");
        }
    }

}