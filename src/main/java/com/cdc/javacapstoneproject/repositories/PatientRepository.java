package com.cdc.javacapstoneproject.repositories;

import com.cdc.javacapstoneproject.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // ✅ Use Optional for safe null checking
    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByEmailOrPhone(String email, String phone);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}

