package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.Appointment.AppointmentRequestDTO;
import com.example.MayaFisioLumiere.Domain.Appointment.AppointmentResponseDTO;
import com.example.MayaFisioLumiere.Entity.AppointmentEntity;
import com.example.MayaFisioLumiere.Entity.PatientEntity;
import com.example.MayaFisioLumiere.Repository.AppointmentRepository;
import com.example.MayaFisioLumiere.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    // criar horario dentro da maya
    public AppointmentEntity createAppointment(AppointmentRequestDTO data) {
        PatientEntity patient = patientRepository.findById(data.patient_id())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        AppointmentEntity newAppointment = new AppointmentEntity();
        newAppointment.setDate(data.date());
        newAppointment.setTime(data.time());
        newAppointment.setPatient(patient);
        newAppointment.setDescription(data.description());

        return appointmentRepository.save(newAppointment);
    }

    // update by patient id
    public AppointmentResponseDTO updateAppointment(AppointmentRequestDTO data, UUID patient_id, UUID appointment_id) {
        AppointmentEntity appointment = appointmentRepository.findById(appointment_id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (!appointment.getPatient().getPatient_ID().equals(patient_id)) {
            throw new RuntimeException("Este agendamento não pertence ao paciente informado.");
        }

        if (data.date() != null) {
            appointment.setDate(data.date());
        }
        if (data.time() != null) {
            appointment.setTime(data.time());
        }
        if (data.description() != null) {
            appointment.setDescription(data.description());
        }

        AppointmentEntity updatedAppointment = appointmentRepository.save(appointment);

        return new AppointmentResponseDTO(
                updatedAppointment.getAppointment_id(),
                updatedAppointment.getDate(),
                updatedAppointment.getTime(),
                updatedAppointment.getDescription(),
                updatedAppointment.getPatient().getPatient_ID()
        );
    }

    public List<AppointmentResponseDTO> getAppointmentsByPatient(UUID patient_id) {
        PatientEntity patient = patientRepository.findById(patient_id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        List<AppointmentEntity> appointments = appointmentRepository.findByPatient(patient.getPatient_ID());

        return appointments.stream().map(entity -> new AppointmentResponseDTO(
                entity.getAppointment_id(),
                entity.getDate(),
                entity.getTime(),
                entity.getDescription(),
                entity.getPatient().getPatient_ID()
        )).toList();
    }

    //get appointments by day

    public List<AppointmentResponseDTO> getAppointmentsByDate(LocalDateTime date) {
        List<AppointmentEntity> appointments = appointmentRepository.findByDate(date);

        return appointments.stream().map(entity -> new AppointmentResponseDTO(
                entity.getAppointment_id(),
                entity.getDate(),
                entity.getTime(),
                entity.getDescription(),
                entity.getPatient().getPatient_ID()
        )).toList();
    }

    // get appointments by month
    public List<AppointmentResponseDTO> getAppointmentsByMonth(int month, int year) {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .filter(a -> a.getDate().getMonthValue() == month && a.getDate().getYear() == year)
                .map(entity -> new AppointmentResponseDTO(
                        entity.getAppointment_id(),
                        entity.getDate(),
                        entity.getTime(),
                        entity.getDescription(),
                        entity.getPatient().getPatient_ID()
                )).toList();
    }

    public List<AppointmentResponseDTO> getAppointmentsByYear(int year) {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .filter(a -> a.getDate().getYear() == year)
                .map(entity -> new AppointmentResponseDTO(
                        entity.getAppointment_id(),
                        entity.getDate(),
                        entity.getTime(),
                        entity.getDescription(),
                        entity.getPatient().getPatient_ID()
                )).toList();
    }

    // get all appointments
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();

        return appointments.stream().map(entity -> new AppointmentResponseDTO(
                entity.getAppointment_id(),
                entity.getDate(),
                entity.getTime(),
                entity.getDescription(),
                entity.getPatient().getPatient_ID()
        )).toList();
    }

    // delete appointment relacionado ao paciente
    public void deleteAppointment(UUID appointment_id, UUID patient_id) {
        AppointmentEntity appointment = appointmentRepository.findById(appointment_id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (!appointment.getPatient().getPatient_ID().equals(patient_id)) {
            throw new RuntimeException("Este agendamento não pertence ao paciente informado.");
        }

        appointmentRepository.delete(appointment);
    }
}