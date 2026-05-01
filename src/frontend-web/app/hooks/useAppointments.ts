import { useState, useEffect, useCallback } from 'react';

export interface AppointmentResponse {
  appointment_id: string;
  date: string;
  time: string;
  description: string;
  patient_id: string;
}

export function useAppointments(patientId?: string) {
  const [isSavingAppointment, setIsSavingAppointment] = useState(false);
  const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
  const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  const fetchAppointments = useCallback(async () => {
    if (!patientId) return;
    try {
      const res = await fetch(`${API_URL}/api/appointment/patient/${patientId}`);
      if (res.ok) {
        const data = await res.json();
        setAppointments(data);
      }
    } catch (e) {
      console.error('Erro ao buscar consultas:', e);
    }
  }, [patientId, API_URL]);

  useEffect(() => {
    fetchAppointments();
  }, [fetchAppointments]);

  const createAppointment = async (data: unknown) => {
    setIsSavingAppointment(true);
    try {
      const res = await fetch(`${API_URL}/api/appointment/create`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });

      if (res.ok) {
        alert('Consulta agendada com sucesso!');
        await fetchAppointments();
        return true;
      }
      return false;
    } catch (e) {
      console.error(e);
      return false;
    } finally {
      setIsSavingAppointment(false);
    }
  };

  return { createAppointment, isSavingAppointment, appointments, fetchAppointments };
}