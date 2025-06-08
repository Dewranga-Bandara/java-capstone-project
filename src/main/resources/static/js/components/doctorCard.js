import { getPatientData } from "../services/patientServices.js";
import { deleteDoctor } from "../services/doctorService.js";

export function createDoctorCard(doctor) {
    const card = document.createElement("div");
    card.className = "bg-white rounded-lg shadow-md p-4 w-full sm:w-[300px]";

    card.innerHTML = `
        <h3 class="text-lg font-bold text-gray-800">${doctor.name}</h3>
        <p class="text-gray-600">Specialty: ${doctor.specialty}</p>
        <p class="text-gray-600">Experience: ${doctor.experience} years</p>
        <p class="text-gray-600">Email: ${doctor.email}</p>
        <p class="text-gray-600">Contact: ${doctor.contact}</p>
        <div class="mt-4 flex space-x-2"></div>
    `;

    const buttonContainer = card.querySelector("div.mt-4");
    const role = localStorage.getItem("role");

    if (role === "admin") {
        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.className = "bg-red-500 text-white px-4 py-1 rounded hover:bg-red-600";
        deleteBtn.onclick = async () => {
            try {
                await deleteDoctor(doctor.id);
                card.remove();
            } catch (error) {
                console.error("Delete failed:", error);
            }
        };
        buttonContainer.appendChild(deleteBtn);
    } else if (role === "patient") {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";
        bookBtn.className = "bg-blue-500 text-white px-4 py-1 rounded hover:bg-blue-600";
        bookBtn.onclick = () => {
            alert("Please log in first!");
            window.location.href = "/pages/login.html";
        };
        buttonContainer.appendChild(bookBtn);
    } else if (role === "loggedPatient") {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";
        bookBtn.className = "bg-green-500 text-white px-4 py-1 rounded hover:bg-green-600";
        bookBtn.onclick = async () => {
            const patient = await getPatientData();
            // Inline booking overlay logic
            const overlay = document.createElement("div");
            overlay.className = "fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50";

            const modal = document.createElement("div");
            modal.className = "bg-white rounded-lg shadow-lg p-6 w-[90%] sm:w-[400px]";
            modal.innerHTML = `
                <h2 class="text-xl font-bold mb-4">Book Appointment</h2>
                <p><strong>Doctor:</strong> ${doctor.name}</p>
                <p><strong>Patient:</strong> ${patient.name}</p>
                <input type="date" id="appointmentDate" class="border rounded px-2 py-1 w-full mt-4"/>
                <div class="mt-4 flex justify-end space-x-2">
                    <button id="confirmBookingBtn" class="bg-green-600 text-white px-4 py-1 rounded hover:bg-green-700">Book</button>
                    <button id="cancelBookingBtn" class="bg-gray-500 text-white px-4 py-1 rounded hover:bg-gray-600">Cancel</button>
                </div>
            `;
            overlay.appendChild(modal);
            document.body.appendChild(overlay);

            document.getElementById("cancelBookingBtn").onclick = () => overlay.remove();
            document.getElementById("confirmBookingBtn").onclick = () => {
                const date = document.getElementById("appointmentDate").value;
                if (!date) {
                    alert("Please select a date.");
                    return;
                }
                alert(`Appointment booked with Dr. ${doctor.name} on ${date}`);
                overlay.remove();
            };
        };
        buttonContainer.appendChild(bookBtn);
    }

    return card;
}
