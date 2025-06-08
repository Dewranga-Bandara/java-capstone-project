export async function getPatientData() {
    const id = localStorage.getItem("patientId");
    const res = await fetch(`/api/patients/${id}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
    });
    if (!res.ok) throw new Error("Failed to fetch patient data");
    return await res.json();
}
