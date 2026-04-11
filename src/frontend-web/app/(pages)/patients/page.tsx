/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";

import { useMemo, useState, useEffect } from "react";
import Select from "react-select";
import { usePatients, PatientRequest } from "@/app/hooks/useGetPatients";
import { useExercises } from "@/app/hooks/useGetExercises";
import { useGetWorkouts } from "@/app/hooks/useGetWorkouts";

export default function PatientsPage() {
  const { patients, removePatient, updatePatient } = usePatients();
  const { exercises } = useExercises();

  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState<PatientRequest | null>(null);

  const filteredPatients = useMemo(() => {
    return patients.filter((p) =>
      `${p.name} ${p.surname}`.toLowerCase().includes(query.toLowerCase())
    );
  }, [patients, query]);

  const selectedPatient = useMemo(() => {
    return (
      patients.find((p) => String(p.patient_id) === String(selectedId)) ??
      filteredPatients[0]
    );
  }, [patients, selectedId, filteredPatients]);

  useEffect(() => {
    setIsEditing(false);
  }, [selectedId]);

  const {
    daysOfWeek,
    workoutSessions,
    exerciseSessions,
    tempExercises,
    scheduleForm,
    selectedDay,
    isSaving,
    setScheduleForm,
    setSelectedDay,
    addExerciseToTempList,
    removeTempExercise,
    saveFullWorkoutToDatabase,
    toggleEditMode,
    updateExerciseSessionLocal,
    deleteExerciseSession,
    saveAllWorkoutChanges,
  } = useGetWorkouts(selectedPatient, exercises);

  const handleEditClick = () => {
    if (!selectedPatient) return;
    setEditForm({
      name: selectedPatient.name,
      surname: selectedPatient.surname,
      cpf: selectedPatient.cpf || "",
      email: selectedPatient.email,
      birthDate: selectedPatient.birthDate || "",
      status: selectedPatient.status,
      cellPhone: selectedPatient.cellPhone,
      gender: selectedPatient.gender,
      height: selectedPatient.height,
      weight: selectedPatient.weight,
      description: selectedPatient.description || "",
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
      <div className="col-span-4 md:col-span-7 bg-white h-fit">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Buscar por nome"
          className="w-full rounded-md border border-neutral-300 px-3 py-2 mb-4 outline-none focus:ring-1 focus:ring-blue"
        />
        <div className="overflow-x-auto max-h-96 no-scrollbar rounded-lg border border-neutral-200 py-3">
          <table className="w-full text-left border-collapse">
            <tbody>
              {filteredPatients.map((p) => {
                const isSelected = p.patient_id === selectedPatient?.patient_id;
                return (
                  <tr
                    key={p.patient_id}
                    className={`flex justify-between items-center border-b border-slate-100 mx-3 rounded-lg ${
                      isSelected && "bg-blue/10"
                    }`}
                  >
                    <td className="py-3 px-4">
                      <button
                        onClick={() => setSelectedId(p.patient_id)}
                        className={`transition-all text-left ${
                          isSelected && "text-dark-blue font-bold"
                        }`}
                      >
                        {p.name} {p.surname}
                      </button>
                    </td>
                    <td className="py-3 px-2">
                      <button
                        onClick={() => removePatient(p.patient_id)}
                        className="rounded-md bg-neutral-50 border border-neutral-100 px-3 py-1 hover:bg-red-600 hover:text-white transition-all text-red-600 text-sm font-semibold"
                      >
                        Excluir
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
      <div className="col-span-4 md:col-span-5 flex flex-col gap-3 p-5 rounded-lg border border-neutral-200 bg-white">
        <div className="flex justify-between items-center pb-2">
          <h2 className="text-xl font-bold text-neutral-800">
            Prontuário e Evolução
          </h2>
          {selectedPatient && !isEditing && (
            <button
              onClick={handleEditClick}
              className="text-sm bg-dark-blue text-white px-3 py-1.5 rounded-md hover:bg-blue transition-colors font-semibold"
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
                    value={editForm.weight || ""}
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
                    value={editForm.height || ""}
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
                    className="w-full border border-black/20 p-2 rounded text-sm bg-white"
                    value={editForm.gender || ""}
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
                    value={editForm.description || ""}
                    onChange={(e) =>
                      setEditForm({ ...editForm, description: e.target.value })
                    }
                  />
                </div>
                <div className="col-span-2 flex gap-2 pt-2">
                  <button
                    onClick={handleSaveUpdate}
                    className="flex-1 bg-dark-blue text-white py-2 rounded font-bold hover:bg-blue transition-all"
                  >
                    Salvar
                  </button>
                  <button
                    onClick={() => setIsEditing(false)}
                    className="flex-1 bg-neutral-100 text-black py-2 rounded font-bold hover:bg-neutral-200 transition-all"
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
                      {selectedPatient.email || "--"}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-black/60 uppercase">CPF</p>
                    <p className="text-neutral-800">
                      {selectedPatient.cpf || "--"}
                    </p>
                  </div>
                </div>
                <div className="grid grid-cols-3 gap-2">
                  <div>
                    <p className="text-sm text-black/60 uppercase">Altura</p>
                    <p className="text-neutral-800 text-base">
                      {selectedPatient.height || "--"} m
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-black/60 uppercase">Peso</p>
                    <p className="text-neutral-800 text-base">
                      {selectedPatient.weight || "--"} kg
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-black/60 uppercase">Gênero</p>
                    <p className="text-neutral-800 text-base capitalize">
                      {selectedPatient.gender?.toLowerCase() || "--"}
                    </p>
                  </div>
                </div>
                <div>
                  <p className="text-sm text-black/60 uppercase">
                    Descrição Clínica
                  </p>
                  <p className="mt-1 text-base">
                    {selectedPatient.description ||
                      "Nenhuma observação registrada."}
                  </p>
                </div>
                <div className="flex items-center gap-2 pt-2">
                  <span className="text-sm text-black/60 uppercase">
                    Status:
                  </span>
                  <span
                    className={`px-2 py-1 rounded ${
                      selectedPatient.status === "ATIVO"
                        ? "bg-blue-100 text-dark-blue"
                        : "bg-neutral-200 text-black"
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

      {/* Calendário do Paciente */}
      <div className="col-span-4 md:col-span-12 mt-6">
        <h2 className="text-xl font-bold border-t pt-4 border-neutral-200">
          Calendário do Paciente
        </h2>
        <div className="flex gap-2 mt-4 flex-wrap">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-md border transition-all ${
                selectedDay === day
                  ? "bg-blue text-white border-blue shadow-md"
                  : "bg-white border-neutral-200 hover:bg-neutral-50"
              }`}
            >
              {day}
            </button>
          ))}
        </div>

        {/* Formulário de Exercício */}
        <form
          onSubmit={addExerciseToTempList}
          className="mt-6 grid grid-cols-12 gap-3 p-4 rounded-md border border-neutral-200"
        >
          <div className="col-span-12 md:col-span-5">
            <Select
              options={exercises.map((e) => ({
                value: String(e.exercise_id),
                label: e.title,
              }))}
              onChange={(opt) =>
                setScheduleForm((prev) => ({
                  ...prev,
                  exerciseName: opt?.value || "",
                }))
              }
              placeholder="Escolha um exercício"
            />
          </div>
          <input
            type="number"
            placeholder="Séries"
            className="col-span-6 md:col-span-2 px-3 py-1 border border-black/40 rounded-md placeholder:text-gray-600!"
            value={scheduleForm.serie}
            onChange={(e) =>
              setScheduleForm((prev) => ({ ...prev, serie: e.target.value }))
            }
            required
          />
          <input
            type="number"
            placeholder="Reps"
            className="col-span-6 md:col-span-2 px-3 py-1 border border-black/40 rounded-md placeholder:text-gray-600!"
            value={scheduleForm.repetitions}
            onChange={(e) =>
              setScheduleForm((prev) => ({
                ...prev,
                repetitions: e.target.value,
              }))
            }
            required
          />
          <button
            type="submit"
            className="col-span-12 md:col-span-3 bg-dark-blue text-white rounded-md font-bold hover:bg-blue"
          >
            + Adicionar
          </button>
        </form>

        {/* Lista Temporária */}
        {tempExercises.length > 0 && (
          <div className="flex flex-col mt-6 p-4 border border-dashed border-blue/30 rounded-md">
            <h3 className="mb-3 font-semibold text-dark-blue">
              Treino de {selectedDay} (Temporário)
            </h3>
            {tempExercises.map((ex, i) => (
              <div
                key={i}
                className="flex justify-between items-center p-3 mb-2 rounded border border-slate-200"
              >
                <span>
                  <strong>{ex.exerciseTitle}</strong> | {ex.serie} x{" "}
                  {ex.repetitions}
                </span>
                <button
                  onClick={() => removeTempExercise(i)}
                  className="flex items-center justify-center rounded-full hover:bg-light-blue p-3"
                >
                  <img
                    src="/lixo.png"
                    alt="Deletar Exercício"
                    className="w-5 h-5 object-contain"
                  />
                </button>
              </div>
            ))}
            <button
              onClick={saveFullWorkoutToDatabase}
              disabled={isSaving}
              className="self-end bg-dark-blue text-white px-5 py-3 rounded-md font-bold"
            >
              {isSaving ? "Gravando..." : `Confirmar Treino de ${selectedDay}`}
            </button>
          </div>
        )}

        {/* Exercícios Salvos (COM EDIÇÃO E EXCLUSÃO) */}
        <div className="mt-8">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-bold text-neutral-700">
              Exercícios Salvos
            </h3>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {exerciseSessions
              .filter(
                (es) =>
                  workoutSessions.find(
                    (ws) =>
                      String(ws.workoutSession_ID) ===
                      String(es.workoutSession_ID)
                  )?.weekDay === selectedDay
              )
              .map((es) => {
                const ex = exercises.find(
                  (e) => String(e.exercise_id) === es.exercise_ID
                );
                return (
                  <article
                    key={es.exerciseSession_ID}
                    className={`rounded-md border p-4 transition-all ${
                      es.isEditing
                        ? "border-blue bg-blue/5 shadow-inner"
                        : "border-neutral-200 bg-white"
                    }`}
                  >
                    <div className="flex flex-col h-full">
                      <div className="flex justify-between items-start mb-3">
                        <p className="font-semibold text-black">
                          {ex?.title || "Exercício"}
                        </p>
                        <div className="flex gap-2">
                          <button
                            onClick={() =>
                              toggleEditMode(es.exerciseSession_ID)
                            }
                            className={`p-1.5 rounded-md transition-colors hover:bg-light-blue ${
                              es.isEditing
                                ? "bg-blue text-white"
                                : "bg-neutral-100"
                            }`}
                            title="Editar"
                          >
                            <img
                              src="/edit-exercise.png"
                              alt="Editar Exercício"
                              className="w-5 h-5 object-contain"
                            />
                          </button>
                          <button
                            onClick={() =>
                              deleteExerciseSession(es.exerciseSession_ID)
                            }
                            className="p-1.5 bg-neutral-100 text-red-600 rounded-md hover:bg-red-600"
                            title="Excluir"
                          >
                            <img
                              src="/lixo.png"
                              alt="Deletar Exercício"
                              className="w-5 h-5 object-contain"
                            />
                          </button>
                        </div>
                      </div>

                      {es.isEditing ? (
                        <div className="grid grid-cols-2 gap-2 mt-auto">
                          <div>
                            <label className="text-sm text-black/80">
                              Séries
                            </label>
                            <input
                              type="number"
                              className="w-full border border-blue/40 rounded px-2 py-2 text-sm focus:outline-blue"
                              value={es.serie}
                              onChange={(e) =>
                                updateExerciseSessionLocal(
                                  es.exerciseSession_ID,
                                  "serie",
                                  e.target.value
                                )
                              }
                            />
                          </div>
                          <div>
                            <label className="text-sm text-black/80">
                              Reps
                            </label>
                            <input
                              type="number"
                              className="w-full border border-blue/40 rounded px-2 py-2 text-sm focus:outline-blue"
                              value={es.repetitions}
                              onChange={(e) =>
                                updateExerciseSessionLocal(
                                  es.exerciseSession_ID,
                                  "repetitions",
                                  e.target.value
                                )
                              }
                            />
                          </div>
                        </div>
                      ) : (
                        <p className="text-black/60 text-sm mt-auto">
                          Configuração:{" "}
                          <span className="font-medium text-black">
                            {es.serie} x {es.repetitions}
                          </span>
                        </p>
                      )}
                    </div>
                  </article>
                );
              })}
          </div>
          <div className="w-full my-6">
          <button
              onClick={saveAllWorkoutChanges}
              disabled={isSaving}
              className="bg-dark-blue text-white px-4 py-2 rounded-md text-sm font-bold hover:bg-blue transition-all disabled:opacity-50 shadow-sm"
            >
              {isSaving ? "Salvando..." : "Salvar Alterações de " + selectedDay}
            </button>
          </div>

          {/* Mensagem caso vazio */}
          {exerciseSessions.filter(
            (es) =>
              workoutSessions.find(
                (ws) =>
                  String(ws.workoutSession_ID) === String(es.workoutSession_ID)
              )?.weekDay === selectedDay
          ).length === 0 && (
            <p className="text-center text-neutral-400 py-10 border-2 border-dashed border-neutral-100 rounded-lg">
              Nenhum exercício salvo para este dia.
            </p>
          )}
        </div>
      </div>
    </section>
  );
}
