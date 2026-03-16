"use client";

import { useState } from "react";
import { usePatients } from "@/app/hooks/useGetPatients";

type FormState = {
  firstName: string;
  lastName: string;
  birthDate: string;
  cpf: string;
};

const emptyForm: FormState = {
  firstName: "",
  lastName: "",
  birthDate: "",
  cpf: "",
};

export default function AddPatientPage() {
  const { patients, addPatient, removePatient } = usePatients();
  const [form, setForm] = useState<FormState>(emptyForm);

  async function submitPatient(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.firstName || !form.lastName || !form.birthDate || !form.cpf)
      return;

    // Calcular idade a partir da data de nascimento
    const ageDiffMs = Date.now() - new Date(form.birthDate).getTime();
    const ageDate = new Date(ageDiffMs);
    const patientAge = Math.abs(ageDate.getUTCFullYear() - 1970);

    // Gerar credenciais dinamicamente para o backend
    const email = `${form.firstName.toLowerCase()}.${form.lastName.toLowerCase()}@lumiere.com`;
    const password = form.birthDate.split("-").reverse().join("");

    const isSuccess = await addPatient({
      name: form.firstName,
      surname: form.lastName,
      cpf: form.cpf,
      email: email,
      password: password,
      patientAge: patientAge,
    });

    if (isSuccess) {
      setForm(emptyForm);
    }
  }

  return (
    <section className="grid grid-cols-4 gap-4 md:h-[calc(100dvh-2rem)] md:grid-cols-12 md:grid-rows-[auto_auto_minmax(0,1fr)] md:overflow-hidden">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl">Add Patient</h1>
      </header>

      <div className="col-span-4 p-5 md:col-span-8">
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
          <button
            type="submit"
            className="col-span-full mt-3 rounded-md bg-blue p-4 font-semibold text-neutral hover:opacity-70 transition duration-300 ease-in-out"
          >
            Cadastrar paciente
          </button>
        </form>
      </div>

      <aside className="col-span-4 p-5 md:col-span-4">
        <h2 className="text-xl">Credenciais geradas</h2>
        <p className="mt-3 leading-relaxed">
          <span className="font-semibold">Login (Email)</span>:
          nome.sobrenome@lumiere.com
          <br />
          <span className="font-semibold">Senha</span>: DDMMYYYY (data de
          nascimento)
        </p>
      </aside>

      <div className="col-span-4 p-5 md:col-span-12 md:flex md:flex-col h-full">
        <h2 className="text-xl">Pacientes Cadastrados</h2>
        <div className="no-scrollbar mt-4 overflow-x-auto md:flex-1">
          <table className="w-full h-full text-left">
            <thead>
              <tr className="border-b border-slate-200">
                <th className="py-2">Nome Completo</th>
                <th className="py-2">Email</th>
                <th className="py-2">Ação</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((patient) => (
                <tr
                  key={patient.patient_id}
                  className="border-b border-slate-100"
                >
                  <td className="py-2">
                    {patient.name} {patient.surname}
                  </td>
                  <td className="py-2">{patient.email}</td>
                  <td className="py-2">
                    <button
                      onClick={() => removePatient(patient.patient_id)}
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
