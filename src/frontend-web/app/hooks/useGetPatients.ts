import { useState, useEffect, useMemo, useCallback } from "react";

export type PatientResponse = {
  patient_id: string;
  name: string;
  surname: string;
  email: string;
  status: string;
  password?: string;
};

export type PatientRequest = {
  name: string;
  surname: string;
  cpf: string;
  email: string;
  password: string;
  patientAge: number;
};

export function usePatients() {
  const [patients, setPatients] = useState<PatientResponse[]>([]);

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
  const API_USER = process.env.NEXT_PUBLIC_API_USER;
  const API_PASS = process.env.NEXT_PUBLIC_API_PASS;
  const token = process.env.NEXT_PUBLIC_TOKEN;

  const basicAuth = useMemo(() => {
    if (typeof window !== "undefined" && API_USER && API_PASS) {
      return "Basic " + btoa(`${API_USER}:${API_PASS}`);
    }
    return "";
  }, [API_USER, API_PASS]);

  const fetchPatients = useCallback(async () => {
    if (!basicAuth) return;
    try {
      const res = await fetch(`${API_URL}/api/patients/getAllPatients`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      if (res.ok) {
        const data = await res.json();
        setPatients(data);
      }
    } catch (error) {
      console.error("Erro ao buscar pacientes:", error);
    }
  }, [API_URL, basicAuth]);

  useEffect(() => {
    let isMounted = true;

    async function fetchPatients() {
      if (!basicAuth) return;
      try {
        const res = await fetch(`${API_URL}/api/patients/getAllPatients`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });
        if (res.ok) {
          const data = await res.json();
          if (isMounted) {
            setPatients(data);
          }
        }
      } catch (error) {
        console.error("Erro ao buscar pacientes:", error);
      }
    }

    fetchPatients();

    return () => {
      isMounted = false;
    };
  }, [API_URL, basicAuth]);

  const addPatient = async (newPatient: PatientRequest) => {
    try {
      const res = await fetch(`${API_URL}/api/patients/createPatient`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newPatient),
      });

      if (res.ok) {
        const data = await res.json();
        // The entity creation endpoint returns the full entity.
        // We'll normalize it to fit the PatientResponse DTO expected by the UI.
        const formatted: PatientResponse = {
          patient_id: data.patient_ID || data.patient_id,
          name: data.name,
          surname: data.surname,
          email: data.email,
          status: data.status,
        };
        setPatients((prev) => [formatted, ...prev]);
        return true;
      } else {
        const err = await res.text();
        alert("Erro ao criar paciente: " + err);
        return false;
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      return false;
    }
  };

  const removePatient = async (id: string) => {
    try {
      const res = await fetch(`${API_URL}/api/patients/delete/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (res.ok) {
        setPatients((prev) => prev.filter((p) => p.patient_id !== id));
        return true;
      }
      return false;
    } catch (error) {
      console.error("Erro ao deletar paciente:", error);
      return false;
    }
  };

  return {
    patients,
    addPatient,
    removePatient,
  };
}
