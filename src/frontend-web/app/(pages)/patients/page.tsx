/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";

import { useMemo, useState } from "react";
import Select from "react-select";
import { usePatients } from "@/app/hooks/useGetPatients";
import { useExercises, Exercise } from "@/app/hooks/useGetExercises";
import { useGetWorkouts } from "@/app/hooks/useGetWorkouts";

export default function PatientsPage() {
  const { patients, removePatient } = usePatients();
  const { exercises } = useExercises();

  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState("");

  const filteredPatients = useMemo(() => {
    return patients.filter((p) =>
      `${p.name} ${p.surname}`.toLowerCase().includes(query.toLowerCase()),
    );
  }, [patients, query]);

  const selectedPatient =
    patients.find(
      (p) =>
        String((p as any).patient_id || p.patient_ID) === String(selectedId),
    ) ?? filteredPatients[0];

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
  } = useGetWorkouts(selectedPatient, exercises);

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl text-neutral-900">
          Acompanhar Pacientes
        </h1>
      </header>

      {/* Tabela de Pacientes */}
      <div className="col-span-4 p-5 md:col-span-7">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Buscar por nome"
          className="w-full rounded-md border border-neutral-300 px-3 py-2 mb-4 outline-none focus:ring-1 focus:ring-blue"
        />
        <div className="overflow-x-auto max-h-68 no-scrollbar rounded-md border border-black/10 p-4">
          <table className="w-full text-left">
            <tbody>
              {filteredPatients.map((p, i) => {
                const pId = (p as any).patient_id || p.patient_ID;
                const isSelected =
                  String(pId) ===
                  String(
                    (selectedPatient as any)?.patient_id ||
                      selectedPatient?.patient_ID,
                  );
                return (
                  <tr
                    key={i}
                    className={`flex justify-between items-center border-b border-slate-100 rounded-lg ${isSelected && "bg-blue/15"}`}
                  >
                    <td className="py-3 px-4">
                      <button
                        onClick={() => setSelectedId(String(pId))}
                        className={`transition-all text-left ${isSelected && "text-dark-blue font-bold"}`}
                      >
                        {p.name} {p.surname}
                      </button>
                    </td>
                    <td className="py-3 px-2">
                      <button
                        onClick={() => removePatient(String(pId))}
                        className="rounded-md bg-neutral-50 border border-neutral-100 px-3 py-1 hover:bg-red-600 hover:text-white transition-all text-red-600 text-sm"
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

      {/* Detalhes Prontuário */}
      <div className="col-span-4 p-5 md:col-span-5 md:flex md:flex-col gap-3 rounded-lg">
        <h2 className="text-xl font-bold text-neutral-800">
          Prontuário e Evolução
        </h2>
        {selectedPatient ? (
          <div className="mt-3 space-y-3">
            <p>
              <span className="font-semibold">Paciente:</span>{" "}
              {selectedPatient.name} {selectedPatient.surname}
            </p>
            <p>
              <span className="font-semibold">Email:</span>{" "}
              {selectedPatient.email}
            </p>
            <p>
              <span className="font-semibold">Status:</span>{" "}
              <span className="px-2 py-1 bg-neutral-100 rounded text-sm">
                {selectedPatient.status}
              </span>
            </p>
          </div>
        ) : (
          <p className="text-neutral-400">Selecione um paciente.</p>
        )}
      </div>

      {/* Calendário */}
      <div className="col-span-4 p-5 md:col-span-12 space-y-4">
        <h2 className="text-xl font-bold pt-6 border-t border-neutral-200">
          Calendário do Paciente
        </h2>
        <div className="flex gap-2 mt-4 flex-wrap">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-md border transition-all ${selectedDay === day ? "bg-blue text-white border-blue" : "bg-white border-neutral-200 hover:bg-neutral-50"}`}
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
              unstyled
              classNames={{
                control: ({ isFocused }) =>
                  `p-1 border rounded-md transition-all ${
                    isFocused
                      ? "border-blue ring-1 ring-blue"
                      : "border-black/60"
                  }`,
                menu: () =>
                  "mt-2 border border-neutral-200 bg-white rounded-md shadow-lg",
                option: ({ isFocused, isSelected }) =>
                  `px-4 py-2 cursor-pointer ${
                    isSelected
                      ? "bg-blue text-white"
                      : isFocused
                        ? "bg-blue/10 text-dark-blue"
                        : "text-neutral-700"
                  }`,
                valueContainer: () => "gap-1",
                input: () => "text-neutral-800",
              }}
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
              value={
                scheduleForm.exerciseName
                  ? {
                      value: scheduleForm.exerciseName,
                      label: exercises.find(
                        (e) =>
                          String(e.exercise_id) === scheduleForm.exerciseName,
                      )?.title,
                    }
                  : null
              }
              placeholder="Escolha um exercício"
            />
          </div>
          <input
            type="number"
            placeholder="Séries"
            className="col-span-6 md:col-span-2 px-3 py-1 border border-black/60 rounded-md outline-none focus:border-blue"
            value={scheduleForm.serie}
            onChange={(e) =>
              setScheduleForm((prev) => ({ ...prev, serie: e.target.value }))
            }
            required
          />
          <input
            type="number"
            placeholder="Reps"
            className="col-span-6 md:col-span-2 px-3 py-1 border rounded-md outline-none focus:border-blue"
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
            className="col-span-12 md:col-span-3 bg-dark-blue text-white rounded-md font-bold hover:bg-blue transition-all"
          >
            + Adicionar à Lista
          </button>
        </form>

        {/* Lista Temporária */}
        {tempExercises.length > 0 && (
          <div className="flex flex-col mt-6 p-4 border border-dashed border-blue/30 rounded-md">
            <h3 className="mb-3 font-semibold text-dark-blue">
              Treino de {selectedDay} (Temporário)
            </h3>
            <div className="space-y-2 mb-4">
              {tempExercises.map((ex, i) => (
                <div
                  key={i}
                  className="flex justify-between items-center p-3 rounded border border-slate-200 shadow-sm"
                >
                  <span>
                    <span className="font-bold">{ex.exerciseTitle}</span> |{" "}
                    {ex.serie} x {ex.repetitions}
                  </span>
                  <button
                    onClick={() => removeTempExercise(i)}
                    className="text-sm font-bold py-1 hover:opacity-60 px-2 border rounded-full"
                  >
                    X
                  </button>
                </div>
              ))}
            </div>
            <button
              onClick={saveFullWorkoutToDatabase}
              disabled={isSaving}
              className="self-end w-fit bg-dark-blue text-white px-5 py-3 rounded-md font-bold hover:bg-blue shadow-md transition-all"
            >
              {isSaving
                ? "Gravando no Banco..."
                : `Confirmar e Salvar Treino de ${selectedDay}`}
            </button>
          </div>
        )}

        {/* Treinos já Salvos */}
        <div className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {exerciseSessions
              .filter((es) => {
                const parentWorkout = workoutSessions.find(
                  (ws) =>
                    String(ws.workoutSession_ID) ===
                    String(es.workoutSession_ID),
                );
                return parentWorkout?.weekDay === selectedDay;
              })
              .map((es, i) => {
                const exerciseMatch = exercises.find(
                  (e) => String(e.exercise_id) === es.exercise_ID,
                );

                return (
                  <article
                    key={i}
                    className="rounded-md border border-neutral-200 p-4"
                  >
                    <div className="flex items-start justify-between">
                      <div>
                        <p className="font-semibold text-black">
                          {exerciseMatch?.title || "Exercício"}
                        </p>
                        <p className="text-black/60 text-sm mt-1">
                          Configuração:{" "}
                          <span className="font-medium text-black">
                            {es.serie}
                          </span>
                        </p>
                      </div>
                      <div>✓</div>
                    </div>
                  </article>
                );
              })}
          </div>

          {exerciseSessions.filter(
            (es) =>
              workoutSessions.find(
                (ws) =>
                  String(ws.workoutSession_ID) === String(es.workoutSession_ID),
              )?.weekDay === selectedDay,
          ).length === 0 && (
            <p className="text-neutral-400">
              Nenhum exercício adicionado a este dia.
            </p>
          )}
        </div>
      </div>
    </section>
  );
}
