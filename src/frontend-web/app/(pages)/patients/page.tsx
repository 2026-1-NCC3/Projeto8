/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";

import { useMemo, useState, useEffect } from "react";
import Select from "react-select";
import { initialSchedule } from "@/app/lib/mock-data";
import { usePatients } from "@/app/hooks/useGetPatients";
import { useExercises } from "@/app/hooks/useGetExercises"; // Integration with your exercises hook!

const daysOfWeek = [
  "Segunda",
  "Terça",
  "Quarta",
  "Quinta",
  "Sexta",
  "Sábado",
  "Domingo",
];

export default function PatientsPage() {
  const { patients, removePatient } = usePatients();
  const { exercises } = useExercises();

  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState("");

  const [schedules, setSchedules] = useState(initialSchedule);
  const [scheduleForm, setScheduleForm] = useState({
    exerciseName: "",
    frequency: "",
  });

  const [workoutSessions, setWorkoutSessions] = useState<
    { workoutSession_ID: string; patient_ID: string; weekDay: string }[]
  >([]);
  const [exerciseSessions, setExerciseSessions] = useState<
    {
      exerciseSession_ID: string;
      workoutSession_ID: string;
      patient_ID: string;
      exercise_ID: string;
      serie: string;
    }[]
  >([]);

  const [selectedDay, setSelectedDay] = useState<string>(daysOfWeek[0]);
  const [active, setActive] = useState<boolean>(false);

  const filteredPatients = useMemo(() => {
    return patients.filter((patient) => {
      const fullName = `${patient.name} ${patient.surname}`.toLowerCase();
      return fullName.includes(query.toLowerCase());
    });
  }, [patients, query]);

  const selectedPatient =
    patients.find((p) => p.patient_id === selectedId) ?? filteredPatients[0];

  function deletePatient(id: string) {
    removePatient(id);
    setSchedules((prev) => prev.filter((item) => item.patientId !== id));
    if (selectedId === id) {
      const next = patients.find((p) => p.patient_id !== id);
      setSelectedId(next?.patient_id ?? "");
    }
  }

  function submitSchedule(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedPatient) return;

    const newWorkoutSession = {
      workoutSession_ID: `WS-${Date.now()}`,
      patient_ID: selectedPatient.patient_id,
      weekDay: selectedDay,
    };

    const newExerciseSession = {
      exerciseSession_ID: `ES-${Date.now()}`,
      workoutSession_ID: newWorkoutSession.workoutSession_ID,
      patient_ID: selectedPatient.patient_id,
      exercise_ID: scheduleForm.exerciseName,
      serie: scheduleForm.frequency,
    };

    setWorkoutSessions((prev) => [...prev, newWorkoutSession]);
    setExerciseSessions((prev) => [...prev, newExerciseSession]);
    setScheduleForm({ exerciseName: "", frequency: "" });
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

        <div className="mt-4 overflow-x-auto max-h-68 no-scrollbar">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-neutral-200">
                <th className="py-2">Paciente</th>
              </tr>
            </thead>
            <tbody>
              {filteredPatients.map((patient) => (
                <tr
                  key={patient.patient_id}
                  className="flex justify-between border-b border-slate-100"
                >
                  <td className="py-3">
                    <button
                      onClick={() => setSelectedId(patient.patient_id)}
                      className="hover:opacity-70 focus:font-bold transition duration-300 ease-in-out"
                    >
                      {patient.name} {patient.surname}
                    </button>
                  </td>
                  <td className="py-3">
                    <div className="flex gap-2">
                      <button
                        onClick={() => deletePatient(patient.patient_id)}
                        className="rounded-md bg-neutral-200 px-3 py-1 hover:opacity-70 transition duration-300 ease-in-out text-red-600"
                      >
                        Excluir
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="col-span-4 p-5 md:col-span-5 md:flex md:flex-col gap-3">
        <h2 className="text-xl">Prontuário e Evolução</h2>
        {selectedPatient ? (
          <div className="mt-3 space-y-3">
            <p>
              <span className="font-semibold">Paciente:</span>{" "}
              {selectedPatient.name} {selectedPatient.surname}
            </p>
            <p>
              <span className="font-semibold">Email associado:</span>{" "}
              {selectedPatient.email}
            </p>
            <p>
              <span className="font-semibold">Status:</span>{" "}
              <span className="bg-blue/10 px-2 py-1 rounded text-sm text-blue">
                {selectedPatient.status}
              </span>
            </p>
            <button className="rounded-md bg-blue px-4 py-2 text-neutral w-full hover:opacity-70 transition duration-300 ease-in-out mt-3">
              Acessar prontuário completo
            </button>
          </div>
        ) : (
          <p className="mt-3 text-neutral-500">
            Selecione um paciente para visualizar o prontuário.
          </p>
        )}
      </div>

      <div className="col-span-4 p-5 md:col-span-12 space-y-4">
        <h2 className="text-xl">Calendário do Paciente Selecionado</h2>

        {/* Tabs dos dias da semana */}
        <div className="flex gap-2 mt-4">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              type="button"
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-2xl border cursor-pointer transition-colors duration-300 ${
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
          onSubmit={submitSchedule}
          className="mt-3 grid grid-cols-4 gap-3 md:grid-cols-12"
        >
          <Select
            options={exercises.map((exercise) => ({
              value: String(
                exercise.exercise_id || (exercise as any).exercise_ID,
              ),
              label: exercise.title,
            }))}
            onChange={(selectedOption) =>
              setScheduleForm((prev) => ({
                ...prev,
                exerciseName: selectedOption ? selectedOption.value : "",
              }))
            }
            placeholder="Selecione um exercício"
            className="col-span-4 md:col-span-3 [&_span]:py-3"
            isSearchable
          />
          <input
            value={scheduleForm.frequency}
            onChange={(event) =>
              setScheduleForm((prev) => ({
                ...prev,
                frequency: event.target.value,
              }))
            }
            placeholder="Série (ex: 3x10)"
            className="col-span-4 rounded-md border border-neutral-300 px-3 md:col-span-3"
          />
          <button
            className="col-span-4 rounded-md bg-blue px-4 py-2 font-semibold text-neutral md:col-span-2 hover:opacity-70 transition duration-300 ease-in-out"
            type="submit"
          >
            Adicionar
          </button>
        </form>

        <div className="mt-4 grid grid-cols-4 gap-3 md:grid-cols-12">
          {workoutSessions
            .filter(
              (ws) =>
                ws.patient_ID === selectedPatient?.patient_id &&
                ws.weekDay === selectedDay,
            )
            .map((workoutSession) => (
              <div
                key={workoutSession.workoutSession_ID}
                className="col-span-4"
              >
                {exerciseSessions
                  .filter(
                    (es) =>
                      es.workoutSession_ID === workoutSession.workoutSession_ID,
                  )
                  .map((exerciseSession) => {
                    const exerciseMatch = exercises.find(
                      (e) =>
                        String(e.exercise_id || (e as any).exercise_ID) ===
                        exerciseSession.exercise_ID,
                    );
                    return (
                      <article
                        key={exerciseSession.exerciseSession_ID}
                        className="rounded-md border border-neutral-200 bg-neutral-50 p-3 mb-2"
                      >
                        <p className="font-semibold">
                          Exercício:{" "}
                          {exerciseMatch ? exerciseMatch.title : "Desconhecido"}
                        </p>
                        <p className="text-neutral-600">
                          Série: {exerciseSession.serie}
                        </p>
                      </article>
                    );
                  })}
              </div>
            ))}
        </div>
      </div>
    </section>
  );
}
