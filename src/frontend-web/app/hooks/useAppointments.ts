import { useState } from 'react';

export interface AppointmentRequest {
    date: string; // ISO String para o LocalDateTime
    time: string;
    patient_id: string;
    description: string;
}

export function useAppointments() {
    const [isSavingAppointment, setIsSavingAppointment] = useState(false);
    const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

    const createAppointment = async (data: AppointmentRequest) => {
        setIsSavingAppointment(true);
        try {
            const res = await fetch(`${API_URL}/api/appointment/create`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (res.ok) {
                alert('Consulta agendada com sucesso!');
                return true;
            } else {
                const error = await res.text();
                alert(`Erro ao agendar: ${error}`);
                return false;
            }
        } catch (e) {
            console.error(e);
            alert('Erro de conexão com o servidor.');
            return false;
        } finally {
            setIsSavingAppointment(false);
        }
    };

    return { createAppointment, isSavingAppointment };
}