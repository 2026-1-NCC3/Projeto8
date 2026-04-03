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
    return patients.filter((patient) => {
      const fullName = `${patient.name} ${patient.surname}`.toLowerCase();
      return fullName.includes(query.toLowerCase());
    });
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

  function deletePatient(id: string) {
    removePatient(id);
    if (String(selectedId) === String(id)) {
      const next = patients.find(
        (p) => String((p as any).patient_id || p.patient_ID) !== String(id),
      );
      setSelectedId(
        next ? String((next as any).patient_id || next.patient_ID) : "",
      );
    }
  }

  function handleSelectPatient(id: string) {
    setSelectedId(String(id));
  }

  function getExerciseId(exercise: Exercise): string {
    return String(exercise.exercise_id);
  }

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl">Acompanhar Pacientes</h1>
      </header>

      <div className="col-span-4 p-5 md:col-span-7">
        <div className="grid grid-cols-4 gap-3 md:grid-cols-12">
          <input
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Buscar por nome"
            className="col-span-4 rounded-md border border-neutral-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
          />
        </div>

        <div className="mt-4 overflow-x-auto max-h-68 no-scrollbar rounded-md border border-black/10 p-4">
          <table className="w-full text-left">
            <tbody>
              {filteredPatients.map((patient, index) => {
                const isSelected =
                  String((patient as any).patient_id || patient.patient_ID) ===
                  String(
                    (selectedPatient as any)?.patient_id ||
                      selectedPatient?.patient_ID,
                  );

                return (
                  <tr
                    key={`${index}-${(patient as any).patient_id || patient.patient_ID}`}
                    className={`flex justify-between items-center rounded-md border-b border-slate-100 ${isSelected && "bg-blue/15"}`}
                  >
                    <td className="py-3 px-4">
                      <button
                        onClick={() =>
                          handleSelectPatient(
                            (patient as any).patient_id || patient.patient_ID,
                          )
                        }
                        className={`transition duration-300 ease-in-out text-left ${isSelected && "text-dark-blue"}`}
                      >
                        {patient.name} {patient.surname}
                      </button>
                    </td>
                    <td className="py-3 px-2">
                      <button
                        onClick={() =>
                          deletePatient(
                            (patient as any).patient_id || patient.patient_ID,
                          )
                        }
                        className="rounded-md bg-neutral-50 border border-neutral-100 px-3 py-1 hover:bg-red-600 hover:text-white transition duration-300 ease-in-out text-red-600"
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

      <div className="col-span-4 p-5 md:col-span-5 md:flex md:flex-col gap-3">
        <h2 className="text-xl font-bold">Prontuário e Evolução</h2>
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
            {selectedPatient.birthDate && (
              <p>
                <span className="font-semibold">Nascimento:</span>{" "}
                {selectedPatient.birthDate}
              </p>
            )}
            {selectedPatient.gender && (
              <p>
                <span className="font-semibold">Gênero:</span>{" "}
                {selectedPatient.gender}
              </p>
            )}
            {selectedPatient.cellPhone && (
              <p>
                <span className="font-semibold">Celular:</span>{" "}
                {selectedPatient.cellPhone}
              </p>
            )}
            {(selectedPatient.height || selectedPatient.weight) && (
              <p>
                <span className="font-semibold">Medidas:</span>{" "}
                {selectedPatient.height && `${selectedPatient.height}m`}
                {selectedPatient.height && selectedPatient.weight && " · "}
                {selectedPatient.weight && `${selectedPatient.weight}kg`}
              </p>
            )}
            <p>
              <span className="font-semibold">Status:</span>{" "}
              <span
                className={`px-2 py-1 rounded text-sm font-medium ${selectedPatient.status === "ATIVO" ? "bg-green-100 text-green-700" : "bg-slate-100 text-slate-500"}`}
              >
                {selectedPatient.status}
              </span>
            </p>
          </div>
        ) : (
          <p className="mt-3 text-neutral-500">
            Selecione um paciente para visualizar.
          </p>
        )}
      </div>

      <div className="col-span-4 p-5 md:col-span-12 space-y-4">
        <h2 className="text-xl font-bold pt-6">
          Calendário do Paciente Selecionado
        </h2>

        <div className="flex gap-2 mt-4 flex-wrap">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              type="button"
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-md border cursor-pointer transition-colors duration-300 ${
                selectedDay === day
                  ? "bg-blue text-neutral border-blue"
                  : "bg-neutral border-light-blue hover:bg-neutral-200"
              }`}
            >
              {day}
            </button>
          ))}
        </div>

        <form
          onSubmit={addExerciseToTempList}
          className="mt-6 grid grid-cols-4 gap-3 md:grid-cols-12 p-4 rounded-md border border-slate-200"
        >
          <Select
            options={exercises.map((exercise) => ({
              value: getExerciseId(exercise),
              label: exercise.title,
            }))}
            onChange={(selectedOption) =>
              setScheduleForm((prev) => ({
                ...prev,
                exerciseName: selectedOption ? selectedOption.value : "",
              }))
            }
            placeholder="Selecione um exercício"
            className="col-span-4 md:col-span-5 [&_span]:py-2"
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
            isSearchable
          />
          <input
            type="number"
            min="1"
            value={scheduleForm.serie}
            onChange={(e) =>
              setScheduleForm((prev) => ({ ...prev, serie: e.target.value }))
            }
            placeholder="Séries (ex: 3)"
            className="col-span-2 rounded-md border border-neutral-300 px-3 md:col-span-2 outline-none focus:border-blue"
            required
          />
          <input
            type="number"
            min="1"
            value={scheduleForm.repetitions}
            onChange={(e) =>
              setScheduleForm((prev) => ({
                ...prev,
                repetitions: e.target.value,
              }))
            }
            placeholder="Repetições (ex: 10)"
            className="col-span-2 rounded-md border border-neutral-300 px-3 md:col-span-2 outline-none focus:border-blue"
            required
          />
          <button
            type="submit"
            className="col-span-4 rounded-md bg-dark-blue px-4 py-2 font-semibold text-white md:col-span-3 hover:bg-blue transition duration-300"
          >
            + Adicionar à Lista
          </button>
        </form>

        {tempExercises.length > 0 && (
          <div className="mt-6 p-4 border border-dashed border-blue/30 rounded-md">
            <h3 className="mb-2 text-black">
              Treino de {selectedDay} (Não Salvo)
            </h3>
            <div className="space-y-2 mb-4">
              {tempExercises.map((ex, i) => (
                <div
                  key={i}
                  className="flex justify-between items-center bg-white p-3 rounded border border-slate-200"
                >
                  <div>
                    <span className="font-semibold">{ex.exerciseTitle}</span> |{" "}
                    <span>
                      {ex.serie} x {ex.repetitions}
                    </span>
                  </div>
                  <div className="flex items-center gap-4">
                    <button
                      onClick={() => removeTempExercise(i)}
                      className="text-red-400 hover:text-red-700 text-sm border border-red-400 rounded-full px-2 py-1"
                    >
                      X
                    </button>
                  </div>
                </div>
              ))}
            </div>
            <button
              onClick={saveFullWorkoutToDatabase}
              disabled={isSaving}
              className="w-full bg-dark-blue text-white py-3 rounded-md font-bold text-lg hover:bg-blue disabled:opacity-50 disabled:cursor-not-allowed transition-all"
            >
              {isSaving ? "Salvando..." : `Salvar Treino de ${selectedDay}`}
            </button>
          </div>
        )}

        <div className="mt-8">
          <div className="grid grid-cols-4 gap-3 md:grid-cols-12">
            {workoutSessions
              .filter(
                (ws) =>
                  String(ws.patient_ID) ===
                    String(
                      (selectedPatient as any)?.patient_id ||
                        selectedPatient?.patient_ID,
                    ) && ws.weekDay === selectedDay,
              )
              .map((workoutSession) => (
                <div
                  key={workoutSession.workoutSession_ID}
                  className="col-span-4 md:col-span-6 lg:col-span-4"
                >
                  {exerciseSessions
                    .filter(
                      (es) =>
                        String(es.workoutSession_ID) ===
                        String(workoutSession.workoutSession_ID),
                    )
                    .map((exerciseSession) => {
                      const exerciseMatch = exercises.find(
                        (e) =>
                          String(e.exercise_id) === exerciseSession.exercise_ID,
                      );
                      return (
                        <article
                          key={exerciseSession.exerciseSession_ID}
                          className="rounded-md border-l-4 border-l-green-500 border border-neutral-200 bg-white shadow-sm p-4 mb-2"
                        >
                          <p className="font-semibold text-neutral-800">
                            {exerciseMatch
                              ? exerciseMatch.title
                              : "Desconhecido"}
                          </p>
                          <p className="text-neutral-500 mt-1">
                            Série:{" "}
                            <span className="font-medium">
                              {exerciseSession.serie}
                            </span>
                          </p>
                        </article>
                      );
                    })}
                </div>
              ))}
          </div>
        </div>
      </div>
    </section>
  );
}
