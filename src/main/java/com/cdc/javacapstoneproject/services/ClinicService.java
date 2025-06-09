package com.cdc.javacapstoneproject.services;

import com.cdc.javacapstoneproject.dto.Login;
import com.cdc.javacapstoneproject.models.*;
import com.cdc.javacapstoneproject.repositories.*;
import com.cdc.javacapstoneproject.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClinicService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public ClinicService(TokenService tokenService,
                         AdminRepository adminRepository,
                         DoctorRepository doctorRepository,
                         PatientRepository patientRepository,
                         DoctorService doctorService,
                         PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 1. Validate Token
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        boolean isValid = tokenService.validateToken(token, user);
        Map<String, String> response = new HashMap<>();

        if (!isValid) {
            response.put("error", "Invalid or expired token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        response.put("message", "Token is valid");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 2. Validate Admin
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(receivedAdmin.getUsername());

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            // You can now proceed with logic like password check, role verification, etc.
            if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                // Successful login or match
                // e.g., generate token, return success response, etc.
                String token = tokenService.generateToken(admin.getUsername());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                // Password mismatch
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid password"));
            }

        } else {
            // Admin not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Admin not found"));
        }
    }

    // 3. Filter Doctor
    public List<Doctor> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecialtyAndTime(name, specialty, time);
    }


    // 4. Validate Appointment
    public int validateAppointment(Appointment appointment) {
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            return -1; // Doctor not provided
        }

        Long doctorId = appointment.getDoctor().getId();
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);

        if (optionalDoctor.isEmpty()) {
            return -1; // Doctor not found
        }

        Doctor doctor = optionalDoctor.get();

        // Extract date and time from LocalDateTime
        LocalDateTime appointmentDateTime = appointment.getAppointmentTime();
        LocalDate date = appointmentDateTime.toLocalDate();
        String time = appointmentDateTime.toLocalTime().toString().substring(0, 5); // format HH:mm

        List<String> availability = doctorService.getDoctorAvailability(doctor.getId(), date);

        if (availability.contains(time)) {
            return 1; // Valid time slot
        }

        return 0; // Time not available
    }



    // 5. Validate Patient (returns true if patient is NOT already registered)
    public boolean validatePatient(Patient patient) {
        Optional<Patient> existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existing.isEmpty();
    }

    // 6. Validate Patient Login
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Optional<Patient> optionalPatient = patientRepository.findByEmail(login.getEmail());
        Map<String, String> response = new HashMap<>();

        if (optionalPatient.isEmpty() || !optionalPatient.get().getPassword().equals(login.getPassword())) {
            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = tokenService.generateToken(login.getEmail());
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 7. Filter Patient Appointments
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        String patientEmail = tokenService.extractEmail(token);
        Optional<Patient> optionalPatient = patientRepository.findByEmail(patientEmail);
        Map<String, Object> response = new HashMap<>();

        if (optionalPatient.isEmpty()) {
            response.put("error", "Patient not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Patient patient = optionalPatient.get();

        if (condition != null && name != null) {
            response.put("appointments", patientService.filterByDoctorAndCondition(name, condition, patient.getId()));
        } else if (condition != null) {
            response.put("appointments", patientService.filterByCondition(condition, patient.getId()));
        } else if (name != null) {
            response.put("appointments", patientService.filterByDoctor(name, patient.getId()));
        } else {
            response.put("appointments", patientService.getAllAppointments(patient.getId()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
