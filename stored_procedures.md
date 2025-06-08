# Clinic Management System - Stored Procedures

This document contains stored procedures for generating reports in a MySQL-based Clinic Management System (CMS).

---

## 📌 Daily Appointment Report by Doctor

**Procedure Name:** `GetDailyAppointmentReportByDoctor`

Generates a report listing all appointments on a specific date, grouped by doctor.

### 🔧 Definition

```sql
DELIMITER $$

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN report_date DATE
)
BEGIN
    SELECT 
        d.name AS doctor_name,
        a.appointment_time,
        a.status,
        p.name AS patient_name,
        p.phone AS patient_phone
    FROM 
        appointment a
    JOIN 
        doctor d ON a.doctor_id = d.id
    JOIN 
        patient p ON a.patient_id = p.id
    WHERE 
        DATE(a.appointment_time) = report_date
    ORDER BY 
        d.name, a.appointment_time;
END$$

DELIMITER ;
```

### ▶️ Run Procedure

```sql
CALL GetDailyAppointmentReportByDoctor('2025-04-15');
```

---

## 📌 Doctor with Most Patients by Month

**Procedure Name:** `GetDoctorWithMostPatientsByMonth`

Identifies the doctor who saw the most patients in a given month and year.

### 🔧 Definition

```sql
DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN input_month INT, 
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id, 
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        MONTH(appointment_time) = input_month 
        AND YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END $$

DELIMITER ;
```

### ▶️ Run Procedure

```sql
CALL GetDoctorWithMostPatientsByMonth(4, 2025);
```

---

## 📌 Doctor with Most Patients by Year

**Procedure Name:** `GetDoctorWithMostPatientsByYear`

Identifies the doctor who saw the most patients in a given year.

### 🔧 Definition

```sql
DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id, 
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END $$

DELIMITER ;
```

### ▶️ Run Procedure

```sql
CALL GetDoctorWithMostPatientsByYear(2025);
```

---

## ✅ Notes

- Use the `DELIMITER` keyword when creating multi-statement stored procedures.
- Replace `doctor_id` with a `JOIN` to the `doctor` table if names are preferred in output.
- Always test each procedure using realistic data.

---