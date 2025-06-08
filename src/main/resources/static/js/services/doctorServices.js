import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = `${API_BASE_URL}/doctor`;

// Get all doctors
export async function getDoctors() {
    try {
        const response = await fetch(`${DOCTOR_API}`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error fetching doctors:", error);
        return [];
    }
}

// Delete a doctor
export async function deleteDoctor(id, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${id}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        const result = await response.json();
        return {
            success: response.ok,
            message: result.message || "Doctor deleted",
        };
    } catch (error) {
        console.error("Error deleting doctor:", error);
        return { success: false, message: "Failed to delete doctor" };
    }
}

// Save (Add) new doctor
export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(doctor),
        });

        const result = await response.json();
        return {
            success: response.ok,
            message: result.message || "Doctor added",
        };
    } catch (error) {
        console.error("Error saving doctor:", error);
        return { success: false, message: "Failed to save doctor" };
    }
}

// Filter doctors by name, time, or specialty
export async function filterDoctors(name = "", time = "", specialty = "") {
    try {
        const url = new URL(`${DOCTOR_API}/filter`, window.location.origin);
        if (name) url.searchParams.append("name", name);
        if (time) url.searchParams.append("time", time);
        if (specialty) url.searchParams.append("specialty", specialty);

        const response = await fetch(url);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error filtering doctors:", error);
        return [];
    }
}
