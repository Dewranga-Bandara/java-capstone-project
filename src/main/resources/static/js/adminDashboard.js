// ================================
// Imports
// ================================
import { openModal } from '../components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';

// ================================
// Event Binding - Add Doctor Button
// ================================
document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
});

// ================================
// On Page Load - Load Doctors
// ================================
document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();
});

// ================================
// Load All Doctors and Render
// ================================
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();

        renderDoctorCards(doctors);
    } catch (error) {
        console.error('Error loading doctor cards:', error);
    }
}

// ================================
// Render Doctor Cards
// ================================
function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = '';

    if (!doctors || doctors.length === 0) {
        contentDiv.innerHTML = '<p>No doctors found</p>';
        return;
    }

    doctors.forEach((doc) => {
        const card = createDoctorCard(doc);
        contentDiv.appendChild(card);
    });
}

// ================================
// Search & Filter Event Binding
// ================================
document.getElementById('searchBar').addEventListener('input', filterDoctorsOnChange);
document.getElementById('filterTime').addEventListener('change', filterDoctorsOnChange);
document.getElementById('filterSpecialty').addEventListener('change', filterDoctorsOnChange);

// ================================
// Filter Doctors Handler
// ================================
async function filterDoctorsOnChange() {
    const searchTerm = document.getElementById('searchBar').value.trim();
    const filterTime = document.getElementById('filterTime').value;
    const filterSpecialty = document.getElementById('filterSpecialty').value;

    try {
        const filtered = await filterDoctors(searchTerm, filterTime, filterSpecialty);
        renderDoctorCards(filtered);
    } catch (error) {
        console.error('Error filtering doctors:', error);
        document.getElementById('content').innerHTML = '<p>No doctors found</p>';
    }
}

// ================================
// Handle Add Doctor Form Submission
// ================================
document.getElementById('addDoctorForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    await adminAddDoctor();
});

// ================================
// Add Doctor Logic
// ================================
async function adminAddDoctor() {
    const name = document.getElementById('docName').value;
    const specialty = document.getElementById('docSpecialty').value;
    const email = document.getElementById('docEmail').value;
    const password = document.getElementById('docPassword').value;
    const mobile = document.getElementById('docMobile').value;
    const availability = Array.from(
        document.querySelectorAll('input[name="availability"]:checked')
    ).map((checkbox) => checkbox.value);

    const token = localStorage.getItem('token');

    if (!token) {
        alert('Unauthorized! Admin login required.');
        return;
    }

    const doctorData = {
        name,
        specialty,
        email,
        password,
        mobile,
        availability
    };

    try {
        const res = await saveDoctor(doctorData, token);

        if (res.success) {
            alert('Doctor added successfully!');
            document.getElementById('addDoctorForm').reset();
            loadDoctorCards(); // Refresh list
        } else {
            alert('Failed to add doctor: ' + res.message);
        }
    } catch (error) {
        console.error('Add doctor error:', error);
        alert('An error occurred while adding the doctor.');
    }
}
