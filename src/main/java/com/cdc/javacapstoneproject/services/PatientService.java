package com.cdc.javacapstoneproject.services;

import com.cdc.javacapstoneproject.dto.AppointmentDTO;
import com.cdc.javacapstoneproject.models.Appointment;
import com.cdc.javacapstoneproject.models.Patient;
import com.cdc.javacapstoneproject.repositories.AppointmentRepository;
import com.cdc.javacapstoneproject.repositories.PatientRepository;
import com.cdc.javacapstoneproject.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    // 1. Create a new patient
    public boolean createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 2. Get all appointments for a patient
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        String email = tokenService.extractEmail(token);
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty() || !patientOpt.get().getId().equals(id)) {
            response.put("message", "Unauthorized or invalid patient ID");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> dtoList = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", dtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 3. Filter appointments by condition (past/future)
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> filtered;

        if (condition.equalsIgnoreCase("past")) {
            filtered = appointments.stream()
                    .filter(app -> app.getAppointmentTime().isBefore(LocalDateTime.now()))
                    .map(AppointmentDTO::new)
                    .collect(Collectors.toList());
        } else if (condition.equalsIgnoreCase("future")) {
            filtered = appointments.stream()
                    .filter(app -> app.getAppointmentTime().isAfter(LocalDateTime.now()))
                    .map(AppointmentDTO::new)
                    .collect(Collectors.toList());
        } else {
            response.put("message", "Invalid condition. Use 'past' or 'future'.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("appointments", filtered);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 4. Filter appointments by doctor's name
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

        List<AppointmentDTO> filtered = appointments.stream()
                .filter(app -> app.getDoctor().getName().toLowerCase().contains(name.toLowerCase()))
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", filtered);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 5. Filter appointments by both doctor name and condition
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, Long patientId) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

        List<AppointmentDTO> filtered = appointments.stream()
                .filter(app -> app.getDoctor().getName().toLowerCase().contains(name.toLowerCase()))
                .filter(app -> {
                    if ("past".equalsIgnoreCase(condition)) {
                        return app.getAppointmentTime().isBefore(LocalDateTime.now());
                    } else if ("future".equalsIgnoreCase(condition)) {
                        return app.getAppointmentTime().isAfter(LocalDateTime.now());
                    }
                    return false;
                })
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        response.put("appointments", filtered);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 6. Get patient details based on JWT token
    public Patient getPatientDetails(String token) {
        String email = tokenService.extractEmail(token);
        return patientRepository.findByEmail(email).orElse(null);
    }

    public List<Appointment> getAllAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public boolean patientExists(String email, String phoneNumber) {
        return patientRepository.existsByEmail(email) || patientRepository.existsByPhoneNumber(phoneNumber);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> filterPatientAppointments(String condition, String name, String token) {
        String email = tokenService.extractEmail(token);
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Long patientId = patientOpt.get().getId();
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

        return appointments.stream()
                .filter(appointment -> {
                    boolean matchesCondition = switch (condition.toLowerCase()) {
                        case "upcoming" -> appointment.getAppointmentTime().isAfter(LocalDateTime.now());
                        case "past" -> appointment.getAppointmentTime().isBefore(LocalDateTime.now());
                        default -> true;
                    };
                    boolean matchesName = name.equalsIgnoreCase("all") ||
                            appointment.getDoctor().getName().toLowerCase().contains(name.toLowerCase());
                    return matchesCondition && matchesName;
                })
                .collect(Collectors.toList());
    }

}
