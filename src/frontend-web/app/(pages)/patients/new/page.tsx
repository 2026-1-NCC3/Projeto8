'use client';

import { useState } from 'react';
import { usePatients } from '@/app/hooks/useGetPatients';

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
  description: string;
};

const emptyForm: FormState = {
  firstName: '',
  lastName: '',
  birthDate: '',
  cpf: '',
  cellPhone: '',
  gender: '',
  height: '',
  weight: '',
  patientAge: '',
  description: '',
};

export default function AddPatientPage() {
  const { addPatient } = usePatients();
  const [form, setForm] = useState<FormState>(emptyForm);
  const [isLoading, setIsLoading] = useState(false);

  function handleChange(field: keyof FormState) {
    return (
      event: React.ChangeEvent<
        HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
      >,
    ) => setForm((prev) => ({ ...prev, [field]: event.target.value }));
  }

  async function submitPatient(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsLoading(true);

    if (!form.firstName || !form.lastName || !form.birthDate || !form.cpf) {
      alert('Por favor, preencha os campos obrigatórios.');
      setIsLoading(false);
      return;
    }

    const email = `${form.firstName.toLowerCase()}.${form.lastName.toLowerCase()}@lumiere.com`;
    const password = form.birthDate.split('-').reverse().join('');

    const isSuccess = await addPatient({
      name: form.firstName,
      surname: form.lastName,
      cpf: form.cpf,
      patientAge: form.patientAge ? parseInt(form.patientAge) : null,
      email,
      password,
      birthDate: form.birthDate,
      status: 'INATIVO',
      cellPhone: form.cellPhone || null,
      gender: form.gender || null,
      height: form.height ? parseFloat(form.height) : null,
      weight: form.weight ? parseFloat(form.weight) : null,
      description: form.description || null,
    });

    if (isSuccess) {
      alert('Paciente cadastrado com sucesso!');
      setForm(emptyForm);
    }
    setIsLoading(false);
  }

  return (
    <section className="grid grid-cols-12 gap-4 min-h-screen">
      <header className="col-span-full">
        <h1 className="font-display text-4xl pt-6">Adicionar Paciente</h1>
      </header>

      <div className="col-span-12 md:col-span-8 bg-white rounded-lg border border-neutral-200 p-6">
        <h2 className="text-xl font-semibold mb-4 text-neutral-800">
          Novo Cadastro
        </h2>
        <form onSubmit={submitPatient} className="grid grid-cols-12 gap-4">
          <input
            value={form.firstName}
            onChange={handleChange('firstName')}
            placeholder="Nome*"
            className="col-span-6 rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
            required
          />
          <input
            value={form.lastName}
            onChange={handleChange('lastName')}
            placeholder="Sobrenome*"
            className="col-span-6 rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
            required
          />
          <input
            type="date"
            value={form.birthDate}
            onChange={handleChange('birthDate')}
            className="col-span-5 rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
            required
          />
          <input
            value={form.cpf}
            onChange={handleChange('cpf')}
            placeholder="CPF*"
            className="col-span-7 rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
            required
          />
          <input
            value={form.cellPhone}
            onChange={handleChange('cellPhone')}
            placeholder="Telemóvel / WhatsApp"
            className="col-span-full rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
          />

          <select
            value={form.gender}
            onChange={handleChange('gender')}
            className="col-span-4 rounded-md border p-3 bg-white border-neutral-300 outline-none focus:border-blue transition-all"
          >
            <option value="">Gênero</option>
            <option value="MASCULINO">Masculino</option>
            <option value="FEMININO">Feminino</option>
          </select>
          <input
            type="number"
            step="0.01"
            value={form.height}
            onChange={handleChange('height')}
            placeholder="Altura (m)"
            className="col-span-4 rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
          />
          <input
            type="number"
            step="0.1"
            value={form.weight}
            onChange={handleChange('weight')}
            placeholder="Peso (kg)"
            className="col-span-4 rounded-md border p-3 border-neutral-300 outline-none focus:border-blue transition-all"
          />

          <textarea
            value={form.description}
            onChange={handleChange('description')}
            placeholder="Anamnese inicial ou descrição do caso clínico..."
            className="col-span-full rounded-md border p-3 border-neutral-300 outline-none focus:border-blue h-28 resize-none transition-all"
          />

          <button
            type="submit"
            disabled={isLoading}
            className="col-span-full bg-dark-blue text-white p-4 rounded-md font-bold hover:bg-blue transition-all disabled:opacity-50 shadow-md"
          >
            {isLoading ? 'A processar...' : 'Confirmar Registo'}
          </button>
        </form>
      </div>

      <aside className="col-span-12 md:col-span-4 bg-blue/5 p-6 rounded-lg border border-blue/10 h-fit">
        <h2 className="text-lg font-bold text-dark-blue">
          Informação de Acesso
        </h2>
        <p className="mt-4 text-black">
          As credenciais do paciente são geradas automaticamente da seguinte
          forma:
          <br />
          <br />
          <strong>Login:</strong> nome.sobrenome@lumiere.com
          <br />
          <strong>Senha:</strong> Data de nascimento (Ddmmyyyy)
        </p>
      </aside>
    </section>
  );
}
