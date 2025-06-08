// 1. Import API Base URL
import { API_BASE_URL } from "../config/config.js";

// 2. Define base API path for patients
const PATIENT_API = `${API_BASE_URL}/patient`;

/**
 * 3. Patient Signup
 * Registers a new patient with their name, email, password, etc.
 * @param {Object} data - Patient details
 * @returns {Object} - { success: Boolean, message: String }
 */
export async function patientSignup(data) {
    try {
        const res = await fetch(`${PATIENT_API}/signup`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
        });

        const result = await res.json();

        if (res.ok) {
            return { success: true, message: result.message };
        } else {
            return { success: false, message: result.message || "Signup failed" };
        }
    } catch (error) {
        console.error("Signup error:", error);
        return { success: false, message: "An error occurred during signup." };
    }
}

/**
 * 4. Patient Login
 * Authenticates a patient with email and password
 * @param {Object} data - { email, password }
 * @returns {Response} - Raw fetch response
 */
export async function patientLogin(data) {
    try {
        const res = await fetch(`${PATIENT_API}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
        });

        return res;
    } catch (error) {
        console.error("Login error:", error);
        return null;
    }
}

/**
 * 5. Get Logged-in Patient Data
 * @param {String} token - Bearer token
 * @returns {Object|null} - Patient data or null
 */
export async function getPatientData(token) {
    try {
        const res = await fetch(`${PATIENT_API}/me`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (res.ok) {
            return await res.json();
        } else {
            return null;
        }
    } catch (error) {
        console.error("Fetch patient data error:", error);
        return null;
    }
}

/**
 * 6. Get Patient Appointments (Patient or Doctor view)
 * @param {String} id - Patient ID
 * @param {String} token - Bearer token
 * @param {String} user - "patient" or "doctor"
 * @returns {Array|null} - Appointments list or null
 */
export async function getPatientAppointments(id, token, user) {
    try {
        const res = await fetch(`${PATIENT_API}/appointments/${user}/${id}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (res.ok) {
            return await res.json();
        } else {
            return null;
        }
    } catch (error) {
        console.error("Fetch appointments error:", error);
        return null;
    }
}

/**
 * 7. Filter Appointments
 * Filters based on condition (e.g. pending, consulted) and patient/doctor name
 * @param {String} condition
 * @param {String} name
 * @param {String} token
 * @returns {Array} - Filtered appointment list or []
 */
export async function filterAppointments(condition, name, token) {
    try {
        const res = await fetch(`${PATIENT_API}/appointments/filter/${condition}/${name}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (res.ok) {
            return await res.json();
        } else {
            console.warn("No filtered data returned");
            return [];
        }
    } catch (error) {
        console.error("Filter appointments error:", error);
        alert("Something went wrong while filtering appointments.");
        return [];
    }
}
