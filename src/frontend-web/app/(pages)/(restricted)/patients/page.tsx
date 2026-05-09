'use client';

import { useMemo, useState, useEffect } from 'react';
import { usePatients, PatientRequest } from '@/app/hooks/useGetPatients';
import { TrashIcon } from '@/public/icons';

export default function PatientsPage() {
  const { patients, removePatient, updatePatient } = usePatients();

  const [query, setQuery] = useState('');
  const [selectedId, setSelectedId] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<PatientRequest | null>(null);

  const filteredPatients = useMemo(() => {
    return patients.filter((p) =>
      `${p.name} ${p.surname}`.toLowerCase().includes(query.toLowerCase()),
    );
  }, [patients, query]);

  const selectedPatient = useMemo(() => {
    return (
      patients.find((p) => String(p.patient_id) === String(selectedId)) ??
      filteredPatients[0]
    );
  }, [patients, selectedId, filteredPatients]);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setIsEditing(false);
  }, [selectedId]);

  const handleEditClick = () => {
    if (!selectedPatient) return;
    setEditForm({
      name: selectedPatient.name,
      surname: selectedPatient.surname,
      cpf: selectedPatient.cpf || '',
      email: selectedPatient.email,
      birthDate: selectedPatient.birthDate || '',
      status: selectedPatient.status,
      cellPhone: selectedPatient.cellPhone,
      gender: selectedPatient.gender,
      height: selectedPatient.height,
      weight: selectedPatient.weight,
      description: selectedPatient.description || '',
      patientAge: null,
    });
    setIsEditing(true);
  };

  const handleSaveUpdate = async () => {
    if (editForm && selectedPatient) {
      const success = await updatePatient(selectedPatient.patient_id, editForm);
      if (success) setIsEditing(false);
    }
  };

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="col-span-full pt-6">
        <h1 className="font-display text-4xl text-neutral-900">
          Acompanhar Pacientes
        </h1>
      </header>

      {/* Tabela de Pacientes */}
      <div className="col-span-4 md:col-span-7 h-fit">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Buscar por nome"
          className="w-full rounded-md border bg-white/30 border-neutral-300 shadow-lg px-3 py-2 mb-4 outline-none focus:ring-1 focus:ring-blue"
        />
        <div className="overflow-x-auto max-h-115 no-scrollbar rounded-lg bg-white/30 border border-neutral-300 shadow-lg py-3">
          <table className="w-full text-left border-collapse">
            <tbody>
              {filteredPatients.map((p) => {
                const isSelected = p.patient_id === selectedPatient?.patient_id;
                return (
                  <tr
                    key={p.patient_id}
                    className={`flex justify-between items-center border-b border-slate-100 mx-3 rounded-lg ${
                      isSelected && 'bg-blue/10'
                    }`}
                  >
                    <td className="py-3 px-4">
                      <button
                        onClick={() => setSelectedId(p.patient_id)}
                        className={`transition-all text-left ${
                          isSelected && 'text-dark-blue font-bold'
                        }`}
                      >
                        {p.name} {p.surname}
                      </button>
                    </td>
                    <td className="py-3 px-2">
                      <button
                        onClick={() => removePatient(p.patient_id)}
                        className="group rounded-md bg-salmon/10 border border-salmon/50 px-3 py-1 hover:bg-salmon hover:text-white transition-all text-salmon text-sm font-semibold"
                      >
                        <TrashIcon className="w-4.5 h-4.5 text-salmon group-hover:text-white transition-colors" />
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>

      {/* Prontuário e Evolução */}
      <div className="col-span-4 md:col-span-5 flex flex-col gap-3 p-5 rounded-lg border border-neutral-300 shadow-lg bg-white/30">
        <div className="flex justify-between items-center pb-2">
          <h2 className="text-xl font-bold text-neutral-800">
            Prontuário e Evolução
          </h2>
          {selectedPatient && !isEditing && (
            <button
              onClick={handleEditClick}
              className="text-sm bg-black text-white px-3 py-1.5 rounded-md hover:opacity-70 transition-colors font-semibold"
            >
              Editar Dados
            </button>
          )}
        </div>

        {selectedPatient ? (
          <div className="space-y-4 pt-2">
            {isEditing && editForm ? (
              <div className="grid grid-cols-2 gap-3">
                <div className="col-span-1">
                  <label className="text-sm text-black/60 uppercase">
                    Nome
                  </label>
                  <input
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.name}
                    onChange={(e) =>
                      setEditForm({ ...editForm, name: e.target.value })
                    }
                  />
                </div>
                <div className="col-span-1">
                  <label className="text-sm text-black/60 uppercase">
                    Sobrenome
                  </label>
                  <input
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.surname}
                    onChange={(e) =>
                      setEditForm({ ...editForm, surname: e.target.value })
                    }
                  />
                </div>
                <div className="col-span-2">
                  <label className="text-sm text-black/60 uppercase">CPF</label>
                  <input
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.cpf}
                    onChange={(e) =>
                      setEditForm({ ...editForm, cpf: e.target.value })
                    }
                  />
                </div>
                <div className="col-span-2">
                  <label className="text-sm text-black/60 uppercase">
                    E-mail
                  </label>
                  <input
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.email}
                    onChange={(e) =>
                      setEditForm({ ...editForm, email: e.target.value })
                    }
                  />
                </div>
                <div className="col-span-1">
                  <label className="text-sm text-black/60 uppercase">
                    Peso (kg)
                  </label>
                  <input
                    type="number"
                    step="0.1"
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.weight || ''}
                    onChange={(e) =>
                      setEditForm({
                        ...editForm,
                        weight: parseFloat(e.target.value),
                      })
                    }
                  />
                </div>
                <div className="col-span-1">
                  <label className="text-sm text-black/60 uppercase">
                    Altura (m)
                  </label>
                  <input
                    type="number"
                    step="0.01"
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.height || ''}
                    onChange={(e) =>
                      setEditForm({
                        ...editForm,
                        height: parseFloat(e.target.value),
                      })
                    }
                  />
                </div>
                <div className="col-span-2">
                  <label className="text-sm text-black/60 uppercase">
                    Gênero
                  </label>
                  <select
                    className="w-full border border-black/20 p-2 rounded text-sm"
                    value={editForm.gender || ''}
                    onChange={(e) =>
                      setEditForm({ ...editForm, gender: e.target.value })
                    }
                  >
                    <option value="">Selecione</option>
                    <option value="MASCULINO">Masculino</option>
                    <option value="FEMININO">Feminino</option>
                  </select>
                </div>
                <div className="col-span-2">
                  <label className="text-sm text-black/60 uppercase">
                    Descrição Clínica
                  </label>
                  <textarea
                    className="w-full border border-black/20 p-2 rounded text-sm h-20 resize-none outline-none focus:border-blue"
                    value={editForm.description || ''}
                    onChange={(e) =>
                      setEditForm({ ...editForm, description: e.target.value })
                    }
                  />
                </div>
                <div className="col-span-2 flex gap-2 pt-2">
                  <button
                    onClick={handleSaveUpdate}
                    className="flex-1 bg-black text-white py-2 rounded hover:bg-blue transition-all"
                  >
                    Salvar
                  </button>
                  <button
                    onClick={() => setIsEditing(false)}
                    className="flex-1 bg-neutral-200 text-black py-2 rounded hover:bg-neutral-300 transition-all"
                  >
                    Cancelar
                  </button>
                </div>
              </div>
            ) : (
              <div className="text-sm space-y-4">
                <div className="grid grid-cols-2 gap-6 border-b border-neutral-100 pb-3">
                  <div>
                    <p className="text-sm text-black/60 uppercase">Email</p>
                    <p className="text-neutral-800 break-all text-base">
                      {selectedPatient.email || '--'}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-black/60 uppercase">CPF</p>
                    <p className="text-neutral-800">
                      {selectedPatient.cpf || '--'}
                    </p>
                  </div>
                </div>
                <div className="grid grid-cols-3 gap-2">
                  <div>
                    <p className="text-sm text-black/60 uppercase">Altura</p>
                    <p className="text-neutral-800 text-base">
                      {selectedPatient.height || '--'} m
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-black/60 uppercase">Peso</p>
                    <p className="text-neutral-800 text-base">
                      {selectedPatient.weight || '--'} kg
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-black/60 uppercase">Gênero</p>
                    <p className="text-neutral-800 text-base capitalize">
                      {selectedPatient.gender?.toLowerCase() || '--'}
                    </p>
                  </div>
                </div>
                <div>
                  <p className="text-sm text-black/60 uppercase">
                    Descrição Clínica
                  </p>
                  <p className="mt-1 text-base">
                    {selectedPatient.description ||
                      'Nenhuma observação registrada.'}
                  </p>
                </div>
                <div className="flex items-center gap-2 pt-2">
                  <span className="text-sm text-black/60 uppercase">
                    Status:
                  </span>
                  <span
                    className={`px-2 py-1 rounded ${
                      selectedPatient.status === 'ATIVO'
                        ? 'bg-dark-blue/50 text-dark-blue'
                        : 'bg-salmon/50 text-black'
                    }`}
                  >
                    {selectedPatient.status}
                  </span>
                </div>
              </div>
            )}
          </div>
        ) : (
          <p className="text-neutral-400 italic">
            Selecione um paciente na lista à esquerda.
          </p>
        )}
      </div>
    </section>
  );
}
