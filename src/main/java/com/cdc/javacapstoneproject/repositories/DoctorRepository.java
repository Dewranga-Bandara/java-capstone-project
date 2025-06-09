package com.cdc.javacapstoneproject.repositories;

import com.cdc.javacapstoneproject.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // ✅ Updated to return Optional
    Optional<Doctor> findByEmail(String email);

    // No changes needed for the rest
    @Query("SELECT d FROM Doctor d WHERE d.name LIKE CONCAT('%', :name, '%')")
    List<Doctor> findByNameLike(String name);

    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

    List<Doctor> findByNameContainingIgnoreCase(String name);
}


