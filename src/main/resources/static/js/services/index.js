import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { selectRole } from "../render.js";

const ENDPOINTS = {
    adminLogin: `${API_BASE_URL}/admin`,
    doctorLogin: `${API_BASE_URL}/doctor/login`,
};

window.onload = function () {
    const adminBtn = document.getElementById('adminLogin');
    const doctorBtn = document.getElementById('doctorLogin');

    if (adminBtn) adminBtn.addEventListener('click', () => openModal('adminLogin'));
    if (doctorBtn) doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
};

async function handleLogin(url, credentials, role) {
    try {
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(credentials),
        });

        if (res.ok) {
            const data = await res.json();
            localStorage.setItem("token", data.token);
            selectRole(role);
        } else {
            alert("Invalid credentials!");
        }
    } catch (err) {
        alert("Login failed!");
        console.error(err);
    }
}

window.adminLoginHandler = function () {
    const username = document.getElementById('adminUsername')?.value;
    const password = document.getElementById('adminPassword')?.value;
    handleLogin(ENDPOINTS.adminLogin, { username, password }, "admin");
};

window.doctorLoginHandler = function () {
    const email = document.getElementById('doctorEmail')?.value;
    const password = document.getElementById('doctorPassword')?.value;
    handleLogin(ENDPOINTS.doctorLogin, { email, password }, "doctor");
};
