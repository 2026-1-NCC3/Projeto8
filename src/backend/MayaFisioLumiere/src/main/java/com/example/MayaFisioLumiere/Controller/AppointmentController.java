package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.Appointment.AppointmentRequestDTO;
import com.example.MayaFisioLumiere.Domain.Appointment.AppointmentResponseDTO;
import com.example.MayaFisioLumiere.Entity.AppointmentEntity;
import com.example.MayaFisioLumiere.Repository.AppointmentRepository;
import com.example.MayaFisioLumiere.Services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private AppointmentService appointmentService;

    // Busca todos os agendamentos dentro do banco de dados
    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments() {
        try {
            List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Criar agendamento pela uuid do paciente
    @GetMapping("/patient/{uuid}")
    public ResponseEntity<?> getAppointmentsByPatient(@PathVariable("uuid") UUID patientId) {
        try {
            List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByPatient(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor");
        }
    }
// busca agendamento do mês do paciente
    @GetMapping("/patient/month/{patient_id}")
    public ResponseEntity<?>  getAppoinmentsforPatientByMonth(@PathVariable("patient_id") UUID patientId, int month, int year) {
        try{
            List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsForPatientMonth(patientId, month, year);
            return ResponseEntity.ok(appointments);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // busca agendamento dessa data
    @GetMapping("/date")
    public ResponseEntity<?> getAppointmentsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao achar agendamento nesse dia");
        }
    }

    // Busca agendamento por mes
    @GetMapping("/month")
    public ResponseEntity<?> getAppointmentsByMonth(@RequestParam int month, @RequestParam int year) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentsByMonth(month, year));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao achar agendamento nesse mês");
        }
    }

    // Busca agendamento por ano
    @GetMapping("/year")
    public ResponseEntity<?> getAppointmentsByYear(@RequestParam int year) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentsByYear(year));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao achar agendamento nesse ano");
        }
    }

    // Cria um agendamento
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequestDTO body) {
        try {
            AppointmentEntity newAppointment = this.appointmentService.createAppointment(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar agendamento");
        }
    }

    // Atualiza agendamento do paciente
    @PutMapping("/update/{appointment_id}/{patient_id}")
    public ResponseEntity<?> updateAppointment(@RequestBody AppointmentRequestDTO data, @PathVariable UUID patient_id, @PathVariable UUID appointment_id) {
        try {
            AppointmentResponseDTO updatedAppointment = appointmentService.updateAppointment(data, patient_id, appointment_id);
            return ResponseEntity.ok(updatedAppointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar agendamento");
        }
    }

    // Deleta agendamento do paciente
    @DeleteMapping("/delete/{appointment_id}/{patient_id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID appointment_id, @PathVariable UUID patient_id) {
        try {
            appointmentService.deleteAppointment(appointment_id, patient_id);
            return ResponseEntity.ok("Agendamento deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar agendamento");
        }
    }
}
