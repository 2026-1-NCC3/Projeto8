"use client";

import { useMemo, useState, useEffect } from "react";
import Select from 'react-select';
import {
  initialPatients,
  initialSchedule,
  type Patient,
} from "../../lib/mock-data";

// Days of the week
const daysOfWeek = ["Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"];

export default function PatientsPage() {
  const [patients, setPatients] = useState<Patient[]>(initialPatients);
  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState(initialPatients[0]?.id ?? "");
  const [schedules, setSchedules] = useState(initialSchedule);
  const [scheduleForm, setScheduleForm] = useState({
    exerciseName: "",
    frequency: "",
  });

  // Estados para gerenciar workoutSessions e exerciseSessions
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
  const [exercises, setExercises] = useState([]);
  const [selectedDay, setSelectedDay] = useState<string>(daysOfWeek[0]);

  useEffect(() => {
    async function fetchExercises() {
      const response = await fetch('/api/exercises'); // Substitua pela sua rota de API
      const data = await response.json();
      setExercises(data);
    }
    fetchExercises();
  }, []);

  const filteredPatients = useMemo(() => {
    return patients.filter((patient) => {
      const fullName = `${patient.firstName} ${patient.lastName}`.toLowerCase();
      return fullName.includes(query.toLowerCase());
    });
  }, [patients, query]);

  const selectedPatient =
    patients.find((patient) => patient.id === selectedId) ??
    filteredPatients[0];
  const patientSchedules = schedules.filter(
    (item) => item.patientId === selectedPatient?.id,
  );

  function deletePatient(id: string) {
    setPatients((prev) => prev.filter((patient) => patient.id !== id));
    setSchedules((prev) => prev.filter((item) => item.patientId !== id));
    if (selectedId === id) {
      const next = patients.find((patient) => patient.id !== id);
      setSelectedId(next?.id ?? "");
    }
  }

  // Função para enviar o agendamento
  function submitSchedule(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedPatient) return;

    // Crie uma nova workoutSession
    const newWorkoutSession = {
      workoutSession_ID: `WS-${Date.now()}`,
      patient_ID: selectedPatient.id,
      weekDay: selectedDay,
    };

    // Crie uma nova exerciseSession associada à workoutSession
    const newExerciseSession = {
      exerciseSession_ID: `ES-${Date.now()}`,
      workoutSession_ID: newWorkoutSession.workoutSession_ID,
      patient_ID: selectedPatient.id,
      exercise_ID: scheduleForm.exerciseName, // Assuma que o exercício é identificado por ID
      serie: scheduleForm.frequency,
    };

    // Atualize os estados
    setWorkoutSessions((prev) => [...prev, newWorkoutSession]);
    setExerciseSessions((prev) => [...prev, newExerciseSession]);

    // Limpe o formulário
    setScheduleForm({ exerciseName: "", frequency: "" });
  }

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl">Acompanhar Pacientes</h1>
      </header>

      <div className="panel col-span-4 p-5 md:col-span-7">
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
                  key={patient.id}
                  className="flex justify-between border-b border-slate-100"
                >
                  <td className="py-3">
                    <button
                      onClick={() => setSelectedId(patient.id)}
                      className="hover:opacity-70 focus:font-bold transition duration-300 ease-in-out"
                    >
                      {patient.firstName} {patient.lastName}
                    </button>
                  </td>
                  <td className="py-3">
                    <div className="flex gap-2">
                      <button
                        onClick={() => deletePatient(patient.id)}
                        className="rounded-md bg-neutral-200 px-3 py-1 hover:opacity-70 transition duration-300 ease-in-out"
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

      <div className="panel col-span-4 p-5 md:col-span-5 md:flex md:flex-col">
        <h2 className="text-xl">Prontuário e Evolução</h2>
        {selectedPatient ? (
          <div className="mt-3 space-y-3">
            <p>
              <span className="font-semibold">Paciente:</span>{" "}
              {selectedPatient.firstName} {selectedPatient.lastName}
            </p>
            <p>
              <span className="font-semibold">Diagnóstico:</span>{" "}
              {selectedPatient.diagnosis}
            </p>
            <p>
              <span className="font-semibold">Última sessão:</span>{" "}
              {selectedPatient.lastSession}
            </p>
            <p>
              <span className="font-semibold">Escala média de dor:</span>{" "}
              {selectedPatient.painLevel}/10
            </p>
            <div>
              <p className="font-semibold">Aderência ao plano</p>
              <div className="h-3 rounded-full bg-neutral-200">
                <div
                  className="h-3 rounded-full bg-black/60"
                  style={{ width: `${selectedPatient.adherence}%` }}
                />
              </div>
              <p className="text-neutral-600">
                {selectedPatient.adherence}% dos exercícios foram completados
              </p>
            </div>
            <button className="rounded-md bg-blue px-4 py-2 text-neutral w-full hover:opacity-70 transition duration-300 ease-in-out">
              Acessar prontuário completo
            </button>
          </div>
        ) : (
          <p className="mt-3 text-neutral-500">
            Selecione um paciente para visualizar o prontuario.
          </p>
        )}
      </div>

      {/* Mostrar o formulário e sessões para o dia selecionado */}
      <div className="panel col-span-4 p-5 md:col-span-12 space-y-4">
        <h2 className="text-xl">Calendário do Paciente Selecionado</h2>

        {/* Tabs dos dias da semana */}
        <div className="flex gap-2 mt-4">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-2xl border border-light-blue bg-neutral cursor-pointer ${selectedDay == day && "bg-blue text-color border-blue"}`}
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
            options={exercises.map((exercise: { id: string, title: string, value: string }) => ({
              value: exercise.id,
              label: exercise.title,
            }))}
            onChange={(selectedOption) =>
              setScheduleForm((prev) => ({
                ...prev,
                exerciseName: selectedOption ? selectedOption.value : '',
              }))
            }
            placeholder="Selecione um exercício"
            className="col-span-4 md:col-span-3"
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
            className="col-span-4 rounded-md border border-neutral-300 px-3 py-2 md:col-span-3"
          />
          <button
            className="col-span-4 rounded-md bg-blue px-4 py-2 font-semibold text-neutral md:col-span-2 hover:opacity-70 transition duration-300 ease-in-out"
            type="submit"
          >
            Adicionar
          </button>
        </form>

        <div className="mt-4 grid grid-cols-4 gap-3 md:grid-cols-12">
          {patientSchedules.length > 0 ? (
            patientSchedules.map((item) => (
              <article
                key={item.id}
                className="col-span-4 rounded-md border border-neutral-200 bg-neutral-50 p-3 md:col-span-4"
              >
                <p className="font-semibold">{item.exerciseName}</p>
                <p className=" text-neutral-600">Frequência: {item.frequency}</p>
              </article>
            ))
          ) : (
            <p className="col-span-4  text-neutral-500 md:col-span-12">
              Nenhum exercicio associado para o paciente selecionado.
            </p>
          )}
        </div>

        {/* Render workout sessions and exercise sessions for the selected day */}
        <div className="mt-4 grid grid-cols-4 gap-3 md:grid-cols-12">
          {workoutSessions
            .filter(
              (ws) =>
                ws.patient_ID === selectedPatient?.id && ws.weekDay === selectedDay,
            )
            .map((workoutSession) => (
              <div key={workoutSession.workoutSession_ID} className="col-span-4">
                {exerciseSessions
                  .filter(
                    (es) =>
                      es.workoutSession_ID === workoutSession.workoutSession_ID,
                  )
                  .map((exerciseSession) => (
                    <article
                      key={exerciseSession.exerciseSession_ID}
                      className="rounded-md border border-neutral-200 bg-neutral-50 p-3"
                    >
                      <p className="font-semibold">
                        Exercício: {exerciseSession.exercise_ID}
                      </p>
                      <p>Série: {exerciseSession.serie}</p>
                    </article>
                  ))}
              </div>
            ))}
        </div>
      </div>
    </section>
  );
}
