import { useState, useEffect, useCallback } from 'react';

export type PatientResponse = {
  patient_id: string;
  name: string;
  surname: string;
  email: string;
  cpf: string;
  status: string;
  birthDate: string;
  cellPhone?: string | null;
  gender?: string | null;
  height?: number | null;
  weight?: number | null;
  description?: string | null;
  lgpdCheck: boolean;
};

export type PatientRequest = {
  name: string;
  surname: string;
  cpf: string;
  email: string;
  patientAge: number | null;
  password?: string;
  birthDate: string;
  status: string;
  cellPhone?: string | null;
  gender?: string | null;
  height?: number | null;
  weight?: number | null;
  description?: string | null;
};

export function usePatients() {
  const [patients, setPatients] = useState<PatientResponse[]>([]);
  const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const normalizePatientsData = (rawData: any[]): PatientResponse[] => {
    return rawData.map((item) => ({
      ...item,
      patient_id: String(item.patient_id ?? item.patient_ID ?? ''),
    }));
  };

  const fetchPatients = useCallback(async () => {
    try {
      const res = await fetch(`${API_URL}/api/patient/getAllPatients`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
      if (res.ok) {
        const rawData = await res.json();
        setPatients(normalizePatientsData(rawData));
      }
    } catch (error) {
      console.error('Erro ao buscar pacientes:', error);
    }
  }, [API_URL]);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchPatients();
  }, [fetchPatients]);

  const addPatient = async (newPatient: PatientRequest): Promise<boolean> => {
    try {
      // Tente mudar para /api/patient/create se /createPatient falhar
      const res = await fetch(`${API_URL}/api/patient/createPatient`, { 
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newPatient),
      });

      if (res.ok) {
        await fetchPatients();
        return true;
      } else {
        // LOG CRÍTICO: Isso vai te mostrar se o erro é 400, 404 ou 500
        const errorBody = await res.text();
        console.error(`Erro do servidor (${res.status}):`, errorBody);
        return false;
      }
    } catch (error) {
      console.error('Erro na requisição POST:', error);
      return false;
    }
  };

  const updatePatient = async (
    id: string,
    body: PatientRequest,
  ): Promise<boolean> => {
    try {
      const res = await fetch(`${API_URL}/api/patient/updatePatient/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      if (res.ok) {
        await fetchPatients();
        return true;
      }
      return false;
    } catch (error) {
      console.error('Erro ao atualizar paciente:', error);
      return false;
    }
  };

  const removePatient = async (id: string): Promise<boolean> => {
    try {
      const res = await fetch(`${API_URL}/api/patient/delete/${id}`, {
        method: 'DELETE',
      });
      if (res.ok) {
        setPatients((prev) => prev.filter((p) => p.patient_id !== id));
        return true;
      }
      return false;
    } catch (error) {
      console.error('Erro ao deletar paciente:', error);
      return false;
    }
  };

  return { patients, fetchPatients, addPatient, updatePatient, removePatient };
}
