"use client";

import { useState } from "react";
import { usePatients } from "@/app/hooks/useGetPatients";

type FormState = {
  firstName: string;
  lastName: string;
  birthDate: string;
  cpf: string;
  cellPhone: string;
  gender: string;
  height: string;
  weight: string;
  patientAge: string;
};

const emptyForm: FormState = {
  firstName: "",
  lastName: "",
  birthDate: "",
  cpf: "",
  cellPhone: "",
  gender: "",
  height: "",
  weight: "",
  patientAge: "",
};

export default function AddPatientPage() {
  const { patients, addPatient, removePatient } = usePatients();
  const [form, setForm] = useState<FormState>(emptyForm);

  function handleChange(field: keyof FormState) {
    return (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
      setForm((prev) => ({ ...prev, [field]: event.target.value }));
  }

  async function submitPatient(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!form.firstName || !form.lastName || !form.birthDate || !form.cpf)
      return;

    const email = `${form.firstName.toLowerCase()}.${form.lastName.toLowerCase()}@lumiere.com`;
    const password = form.birthDate.split("-").reverse().join("");

    const isSuccess = await addPatient({
      name: form.firstName,
      surname: form.lastName,
      cpf: form.cpf,
      patientAge: form.patientAge ? parseInt(form.patientAge) : null,
      email,
      password,
      birthDate: form.birthDate,
      status: "INATIVO",
      cellPhone: form.cellPhone || null,
      gender: form.gender || null,
      height: form.height ? parseFloat(form.height) : null,
      weight: form.weight ? parseFloat(form.weight) : null,
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
          {/* Nome e Sobrenome */}
          <input
            value={form.firstName}
            onChange={handleChange("firstName")}
            placeholder="Nome"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
            required
          />
          <input
            value={form.lastName}
            onChange={handleChange("lastName")}
            placeholder="Sobrenome"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
            required
          />

          {/* Data de Nascimento e CPF */}
          <input
            type="date"
            value={form.birthDate}
            onChange={handleChange("birthDate")}
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-5"
            required
          />
          <input
            value={form.cpf}
            onChange={handleChange("cpf")}
            placeholder="CPF"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-7"
            required
          />

          {/* Telefone e Gênero */}
          <input
            value={form.cellPhone}
            onChange={handleChange("cellPhone")}
            placeholder="Celular (WhatsApp)"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
          />
          <select
            value={form.gender}
            onChange={handleChange("gender")}
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 text-slate-500"
          >
            <option value="">Gênero</option>
            <option value="MASCULINO">Masculino</option>
            <option value="FEMININO">Feminino</option>
            <option value="OUTRO">Outro</option>
          </select>

          {/* Altura e Peso */}
          <input
            type="number"
            step="0.01"
            value={form.height}
            onChange={handleChange("height")}
            placeholder="Altura (m)"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
          />
          <input
            type="number"
            step="0.1"
            value={form.weight}
            onChange={handleChange("weight")}
            placeholder="Peso (kg)"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
          />
          <input
            type="number"
            value={form.patientAge}
            onChange={handleChange("patientAge")}
            placeholder="Idade"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6"
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
          <br />
          <span className="font-semibold">Status inicial</span>: INATIVO
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
                <th className="py-2">Status</th>
                <th className="py-2">Ação</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((patient) => (
                <tr
                  key={patient.patient_ID}
                  className="border-b border-slate-100"
                >
                  <td className="py-2">
                    {patient.name} {patient.surname}
                  </td>
                  <td className="py-2">{patient.email}</td>
                  <td className="py-2">
                    <span
                      className={`rounded-full px-2 py-0.5 text-sm font-medium ${
                        patient.status === "ATIVO"
                          ? "bg-green-100 text-green-700"
                          : "bg-slate-100 text-slate-500"
                      }`}
                    >
                      {patient.status}
                    </span>
                  </td>
                  <td className="py-2">
                    <button
                      onClick={() => removePatient(patient.patient_ID)}
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
