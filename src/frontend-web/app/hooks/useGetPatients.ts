import { useState, useEffect, useCallback } from "react";

export type PatientResponse = {
  patient_ID: string;
  name: string;
  surname: string;
  email: string;
  cpf: string;
  status: string;
  birthDate?: string;
  cellPhone?: string | null;
  gender?: string | null;
  height?: number | null;
  weight?: number | null;
};

export type PatientRequest = {
  name: string;
  surname: string;
  cpf: string;
  email: string;
  patientAge: number | null;
  password: string;
  birthDate: string;
  status: string;
  cellPhone?: string | null;
  gender?: string | null;
  height?: number | null;
  weight?: number | null;
};

export function usePatients() {
  const [patients, setPatients] = useState<PatientResponse[]>([]);

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const normalizePatientsData = (rawData: any[]): PatientResponse[] => {
    return rawData.map((item) => ({
      ...item,
      patient_ID: String(item.patient_ID ?? item.patient_id ?? ""),
    }));
  };

  const fetchPatients = useCallback(async () => {
    try {
      const res = await fetch(`${API_URL}/api/patient/getAllPatients`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (res.ok) {
        const rawData = await res.json();
        setPatients(normalizePatientsData(rawData));
      }
    } catch (error) {
      console.error("Erro ao buscar pacientes:", error);
    }
  }, [API_URL]);

  useEffect(() => {
    let isMounted = true;

    async function load() {
      try {
        const res = await fetch(`${API_URL}/api/patient/getAllPatients`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });
        if (res.ok) {
          const rawData = await res.json();
          if (isMounted) setPatients(normalizePatientsData(rawData));
        }
      } catch (error) {
        console.error("Erro ao buscar pacientes:", error);
      }
    }

    load();
    return () => {
      isMounted = false;
    };
  }, [API_URL]);

  const addPatient = async (newPatient: PatientRequest): Promise<boolean> => {
    try {
      const token =
        typeof window !== "undefined" ? localStorage.getItem("token") : "";

      const res = await fetch(`${API_URL}/api/patient/createPatient`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(newPatient),
      });

      if (res.ok) {
        const data = await res.json();
        const formatted: PatientResponse = {
          patient_ID: String(data.patient_ID ?? data.patient_id),
          name: data.name,
          surname: data.surname,
          email: data.email,
          cpf: data.cpf,
          status: data.status,
          birthDate: data.birthDate,
          cellPhone: data.cellPhone ?? null,
          gender: data.gender ?? null,
          height: data.height ?? null,
          weight: data.weight ?? null,
        };
        setPatients((prev) => [formatted, ...prev]);
        return true;
      }

      const err = await res.text();
      alert("Erro ao criar paciente: " + err);
      return false;
    } catch (error) {
      console.error("Erro na requisição:", error);
      return false;
    }
  };

  const removePatient = async (id: string): Promise<boolean> => {
    try {
      const res = await fetch(`${API_URL}/api/patient/delete/${id}`, {
        method: "DELETE",
      });
      if (res.ok) {
        setPatients((prev) => prev.filter((p) => p.patient_ID !== id));
        return true;
      }
      return false;
    } catch (error) {
      console.error("Erro ao deletar paciente:", error);
      return false;
    }
  };

  return { patients, fetchPatients, addPatient, removePatient };
}
