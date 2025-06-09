package com.cdc.javacapstoneproject.services;

import com.cdc.javacapstoneproject.models.Appointment;
import com.cdc.javacapstoneproject.models.Doctor;
import com.cdc.javacapstoneproject.models.Patient;
import com.cdc.javacapstoneproject.repositories.AppointmentRepository;
import com.cdc.javacapstoneproject.repositories.DoctorRepository;
import com.cdc.javacapstoneproject.repositories.PatientRepository;
import com.cdc.javacapstoneproject.utils.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClinicService clinicService;

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());

        if (existing.isEmpty()) {
            response.put("message", "Appointment not found.");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
        if (doctorOpt.isEmpty()) {
            response.put("message", "Invalid doctor ID.");
            return ResponseEntity.badRequest().body(response);
        }

        LocalDateTime appointmentTime = appointment.getAppointmentTime();
        List<Appointment> overlappingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTime(
                appointment.getDoctor().getId(), appointmentTime);

        boolean isDuplicate = overlappingAppointments.stream()
                .anyMatch(a -> !a.getId().equals(appointment.getId()));

        if (isDuplicate) {
            response.put("message", "Appointment slot is already booked.");
            return ResponseEntity.badRequest().body(response);
        }

        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id,
                                                                 @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return appointmentService.cancelAppointment(id, token);
    }


    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format. Use yyyy-MM-dd."));
        }

        Map<String, Object> result = appointmentService.getAppointment(patientName, localDate, token);
        return ResponseEntity.ok(result);
    }

    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        // Extract email from token
        String emailFromToken = tokenService.extractEmail(token);

        // Find the doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(emailFromToken);
        if (doctorOpt.isEmpty()) {
            // Return empty appointment list if doctor is not found
            return Map.of("appointments", List.of());
        }

        Doctor doctor = doctorOpt.get();

        // Define time range for the selected date
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        // Get all appointments for the doctor on that date
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctor.getId(), start, end
        );

        // Optional filtering by patient name (case-insensitive contains)
        if (pname != null && !pname.isBlank()) {
            appointments = appointments.stream()
                    .filter(a -> a.getPatient().getName().toLowerCase().contains(pname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Return result wrapped in a map
        return Map.of("appointments", appointments);
    }


}
