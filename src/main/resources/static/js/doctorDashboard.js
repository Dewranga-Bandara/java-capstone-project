// ================================
// Imports
// ================================
import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

// ================================
// Global Variables
// ================================
const tableBody = document.getElementById('patientTableBody');
let selectedDate = new Date().toISOString().split('T')[0]; // Today's date in yyyy-mm-dd format
const token = localStorage.getItem('token'); // Doctor's token
let patientName = null;

// ================================
// Search Bar - Filter by Patient Name
// ================================
document.getElementById('searchBar').addEventListener('input', (e) => {
    const input = e.target.value.trim();
    patientName = input === '' ? null : input;
    loadAppointments();
});

// ================================
// Today Button - Filter by Today
// ================================
document.getElementById('todayButton').addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];
    document.getElementById('datePicker').value = selectedDate;
    loadAppointments();
});

// ================================
// Date Picker Change - Custom Date Filter
// ================================
document.getElementById('datePicker').addEventListener('change', (e) => {
    selectedDate = e.target.value;
    loadAppointments();
});

// ================================
// Load Appointments Function
// ================================
async function loadAppointments() {
    tableBody.innerHTML = ''; // Clear table content

    try {
        const appointments = await getAllAppointments(selectedDate, patientName, token);

        if (!appointments || appointments.length === 0) {
            tableBody.innerHTML = `
                <tr><td colspan="6" class="text-center">No Appointments found for selected date</td></tr>
            `;
            return;
        }

        appointments.forEach((appointment) => {
            const row = createPatientRow(appointment);
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error('Error fetching appointments:', error);
        tableBody.innerHTML = `
            <tr><td colspan="6" class="text-center text-danger">Failed to load appointments. Please try again.</td></tr>
        `;
    }
}

// ================================
// Initial Render on Page Load
// ================================
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('datePicker').value = selectedDate; // Set date picker default
    loadAppointments();
});
