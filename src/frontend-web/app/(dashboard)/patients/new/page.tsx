"use client";

import { useMemo, useState } from "react";
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

  const generatedLogin = useMemo(() => {
    if (!form.firstName || !form.lastName) return "firstName.lastName";
    return `${form.firstName}.${form.lastName}`.toLowerCase().replace(/\s+/g, "");
  }, [form.firstName, form.lastName]);

  function submitPatient(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.firstName || !form.lastName || !form.birthDate || !form.cpf) return;

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
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="panel col-span-4 p-6 md:col-span-12">
        <h1 className="font-[family-name:var(--font-display)] text-[34px] text-[var(--dark-blue)]">Add Patient</h1>
        <p className="mt-2 text-slate-600">Cadastro de paciente com credenciais iniciais para acesso no app.</p>
      </header>

      <form onSubmit={submitPatient} className="panel col-span-4 grid grid-cols-4 gap-3 p-5 md:col-span-7 md:grid-cols-12">
        <input
          value={form.firstName}
          onChange={(event) => setForm((prev) => ({ ...prev, firstName: event.target.value }))}
          placeholder="Nome"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-6"
          required
        />
        <input
          value={form.lastName}
          onChange={(event) => setForm((prev) => ({ ...prev, lastName: event.target.value }))}
          placeholder="Sobrenome"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-6"
          required
        />
        <input
          type="date"
          value={form.birthDate}
          onChange={(event) => setForm((prev) => ({ ...prev, birthDate: event.target.value }))}
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-4"
          required
        />
        <input
          value={form.cpf}
          onChange={(event) => setForm((prev) => ({ ...prev, cpf: event.target.value }))}
          placeholder="CPF"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-4"
          required
        />
        <input
          value={form.diagnosis}
          onChange={(event) => setForm((prev) => ({ ...prev, diagnosis: event.target.value }))}
          placeholder="Diagnostico inicial"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-12"
        />
        <button type="submit" className="col-span-4 rounded-lg bg-[var(--dark-blue)] px-4 py-2 font-semibold text-white md:col-span-4">
          Cadastrar paciente
        </button>
      </form>

      <aside className="panel col-span-4 p-5 md:col-span-5">
        <h2 className="font-semibold text-[22px] text-[var(--dark-blue)]">Credenciais geradas</h2>
        <div className="mt-3 rounded-lg bg-[var(--light-blue)]/50 p-4 text-sm">
          <p><span className="font-semibold">Login:</span> {generatedLogin}</p>
          <p><span className="font-semibold">Senha inicial:</span> {form.birthDate || "birthDate"}</p>
          <p className="mt-2 text-slate-700">Regra obrigatoria aplicada: `login = firstName.lastName` e `senha = birthDate`.</p>
        </div>
      </aside>

      <div className="panel col-span-4 p-5 md:col-span-12">
        <h2 className="font-semibold text-[22px] text-[var(--dark-blue)]">Pacientes cadastrados</h2>
        <div className="mt-4 overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="border-b border-slate-200 text-slate-600">
                <th className="py-2">Nome</th>
                <th className="py-2">CPF</th>
                <th className="py-2">Acao</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((patient) => (
                <tr key={patient.id} className="border-b border-slate-100">
                  <td className="py-2">{patient.firstName} {patient.lastName}</td>
                  <td className="py-2">{patient.cpf}</td>
                  <td className="py-2">
                    <button
                      onClick={() => removePatient(patient.id)}
                      className="rounded-lg bg-[var(--light-salmon)] px-3 py-1"
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
