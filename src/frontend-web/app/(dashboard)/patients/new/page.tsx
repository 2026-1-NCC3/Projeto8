"use client";

import { useState } from "react";
import { initialPatients, type Patient } from "../../../lib/mock-data";

type FormState = {
  firstName: string;
  lastName: string;
  birthDate: string;
  cpf: string;
  diagnosis: string;
};

const emptyForm: FormState = {
  firstName: "",
  lastName: "",
  birthDate: "",
  cpf: "",
  diagnosis: "",
};

export default function AddPatientPage() {
  const [patients, setPatients] = useState<Patient[]>(initialPatients);
  const [form, setForm] = useState<FormState>(emptyForm);

  function submitPatient(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.firstName || !form.lastName || !form.birthDate || !form.cpf)
      return;

    const next: Patient = {
      id: `PAT-${Date.now()}`,
      firstName: form.firstName,
      lastName: form.lastName,
      birthDate: form.birthDate,
      cpf: form.cpf,
      diagnosis: form.diagnosis || "Aguardando avaliacao funcional",
      painLevel: 0,
      adherence: 0,
      lastSession: new Date().toISOString().slice(0, 10),
    };

    setPatients((prev) => [next, ...prev]);
    setForm(emptyForm);
  }

  function removePatient(id: string) {
    setPatients((prev) => prev.filter((patient) => patient.id !== id));
  }

  return (
    <section className="grid grid-cols-4 gap-4 md:h-[calc(100dvh-2rem)] md:grid-cols-12 md:grid-rows-[auto_auto_minmax(0,1fr)] md:overflow-hidden">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl">Add Patient</h1>
      </header>

      <div className=" col-span-4 p-5 md:col-span-8">
        <h2 className="text-xl">Adicionar um novo paciente</h2>
        <form
          onSubmit={submitPatient}
          className="grid grid-cols-4 gap-3 md:grid-cols-12 mt-3"
        >
          <input
            value={form.firstName}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, firstName: event.target.value }))
            }
            placeholder="Nome"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
            required
          />
          <input
            value={form.lastName}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, lastName: event.target.value }))
            }
            placeholder="Sobrenome"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
            required
          />
          <input
            type="date"
            value={form.birthDate}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, birthDate: event.target.value }))
            }
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-5"
            required
          />
          <input
            value={form.cpf}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, cpf: event.target.value }))
            }
            placeholder="CPF"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-7"
            required
          />
          <input
            value={form.diagnosis}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, diagnosis: event.target.value }))
            }
            placeholder="Diagnostico inicial"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-12"
          />
          <button
            type="submit"
            className="col-span-full mt-3 rounded-md bg-blue p-4 font-semibold text-neutral hover:opacity-70 transition duration-300 ease-in-out"
          >
            Cadastrar paciente
          </button>
        </form>
      </div>

      {/* Explicação sobre credenciais */}
      <aside className="col-span-4 p-5 md:col-span-4">
        <h2 className="text-xl">Credenciais geradas</h2>
        <p className="mt-3 leading-relaxed">
          <span className="font-semibold">Login</span>: nome.sobrenome
          <br />
          <span className="font-semibold">Senha</span>: DDMMYYY (dada de
          nascimento)
        </p>
      </aside>

      {/* Pacientes existentes */}
      <div className="col-span-4 p-5 md:col-span-12 md:flex md:flex-col h-full">
        <h2 className="text-xl">Pacientes Cadastrados</h2>
        <div className="no-scrollbar mt-4 overflow-x-auto md:flex-1">
          <table className="w-full h-full text-left">
            <thead>
              <tr className="border-b border-slate-200">
                <th className="py-2">Nome Completo</th>
                <th className="py-2">CPF</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((patient) => (
                <tr key={patient.id} className="border-b border-slate-100">
                  <td className="py-2">
                    {patient.firstName} {patient.lastName}
                  </td>
                  <td className="py-2">{patient.cpf}</td>
                  <td className="py-2">
                    <button
                      onClick={() => removePatient(patient.id)}
                      className="rounded-md bg-neutral-200 px-3 py-1 hover:opacity-70 transition duration-300 ease-in-out"
                    >
                      Excluir
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}
