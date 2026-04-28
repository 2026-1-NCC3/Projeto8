/* eslint-disable @typescript-eslint/no-explicit-any */
'use client';

import { useMemo, useState, useEffect } from 'react';
import Select from 'react-select';
import Image from 'next/image';
import Calendar from 'react-calendar';

import { usePatients } from '@/app/hooks/useGetPatients';
import { useExercises } from '@/app/hooks/useGetExercises';
import { useGetWorkouts } from '@/app/hooks/useGetWorkouts';
import { useAppointments } from '@/app/hooks/useAppointments';

import 'react-calendar/dist/Calendar.css';

const calendarStyles = `
  .react-calendar { 
    width: 100%;
    border: none; 
    font-family: inherit; 
    border-radius: 0.375rem; 
    color: #000000 !important;
    box-shadow: none;
    background: transparent;
  }
  
  /* Estilo para o dia selecionado (Active) */
  .react-calendar__tile--active { 
    background: #0070f3 !important; 
    border-radius: 0.375rem; 
    color: white; 
  }
  
  /* Estilo para o dia ATUAL (Now) */
  .react-calendar__tile--now { 
    background: #0070f3 !important; 
    color: white !important; 
    border-radius: 0.375rem;
    font-weight: bold;
  }

  /* Texto dos dias da semana */
  .react-calendar__month-view__weekdays__weekday,
  .react-calendar__month-view__days__day--weekend {
    text-transform: uppercase;
    font-size: 0.75rem;
    font-weight: bold;
    color: #000000;
  }

  .react-calendar__navigation button { 
    font-size: 1.1rem; 
    font-weight: bold; 
  }
`;

export default function CalendarsPage() {
  const { patients } = usePatients();
  const { exercises } = useExercises();
  const { createAppointment, isSavingAppointment } = useAppointments();

  const [selectedId, setSelectedId] = useState('');
  const [isMounted, setIsMounted] = useState(false);

  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [selectedTime, setSelectedTime] = useState('');
  const [description, setDescription] = useState('');

  useEffect(() => {
    setIsMounted(true);
  }, []);

  const patientOptions = useMemo(() => {
    return patients.map((p) => ({
      value: String(p.patient_id),
      label: `${p.name} ${p.surname}`,
    }));
  }, [patients]);

  const selectedPatient = useMemo(() => {
    return (
      patients.find((p) => String(p.patient_id) === String(selectedId)) || null
    );
  }, [patients, selectedId]);

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

  const handleSchedule = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedPatient || !selectedTime) {
      alert('Selecione um paciente e um horário.');
      return;
    }

    const datePart = selectedDate.toISOString().split('T')[0];
    const isoDateTime = `${datePart}T00:00:00`;

    const success = await createAppointment({
      date: isoDateTime,
      time: selectedTime,
      patient_id: String(selectedPatient.patient_id),
      description: description,
    });

    if (success) {
      setSelectedTime('');
      setDescription('');
    }
  };

  if (!isMounted) return null;

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12 pb-20">
      <style>{calendarStyles}</style>

      <h1 className="font-display text-4xl text-neutral-900 col-span-full pt-6">
        Gerenciar Calendários e Consultas
      </h1>

      <div className="col-span-4 md:col-span-12">
        <h2 className="text-xl font-bold py-4 border-t border-gray-300">
          Selecione o Paciente
        </h2>

        {/* Dropdown de pacientes */}
        <div className="mt-4 max-w-md">
          <Select
            instanceId="patient-select-main"
            options={patientOptions}
            value={
              patientOptions.find((opt) => opt.value === selectedId) || null
            }
            placeholder="Selecione um paciente..."
            isClearable
            onChange={(opt: any) => setSelectedId(opt?.value || '')}
          />
        </div>

        {/* Calendário dos pacientes */}
        {selectedPatient && (
          <>
            <h2 className="text-xl font-bold mt-12 pt-4 border-t border-gray-300">
              Calendário de Exercícios
            </h2>

            <div className="flex gap-2 mt-4 flex-wrap">
              {daysOfWeek.map((day) => (
                <button
                  key={day}
                  onClick={() => setSelectedDay(day)}
                  className={`py-2 px-4 rounded-md border transition-all ${
                    selectedDay === day
                      ? 'bg-dark-blue text-white border-dark-blue shadow-sm'
                      : 'bg-white border-neutral-200 hover:bg-neutral-50'
                  }`}
                >
                  {day}
                </button>
              ))}
            </div>

            {/* Formulário de Exercício */}
            <form
              onSubmit={addExerciseToTempList}
              className="mt-6 grid grid-cols-12 gap-3 p-4 rounded-md bg-white/30 border border-neutral-200"
            >
              <div className="col-span-12 md:col-span-4">
                <Select
                  instanceId="exercise-select-field"
                  options={exercises.map((e) => ({
                    value: String(e.exercise_id),
                    label: e.title,
                  }))}
                  onChange={(opt: any) =>
                    setScheduleForm((prev) => ({
                      ...prev,
                      exerciseName: opt?.value || '',
                    }))
                  }
                  placeholder="Escolha um exercício"
                />
              </div>
              <input
                type="number"
                placeholder="Séries"
                className="col-span-6 md:col-span-2 px-3 py-1 border border-black/20 rounded-md placeholder:text-gray-600!"
                value={scheduleForm.serie}
                onChange={(e) =>
                  setScheduleForm((prev) => ({
                    ...prev,
                    serie: e.target.value,
                  }))
                }
                required
              />
              <input
                type="number"
                placeholder="Reps"
                className="col-span-6 md:col-span-3 px-3 py-1 border border-black/20 rounded-md placeholder:text-gray-600!"
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
                className="col-span-12 md:col-span-3 bg-dark-blue text-white rounded-md font-bold hover:bg-blue transition-colors"
              >
                + Adicionar Exercício
              </button>
            </form>

            {/* Lista Temporária de Treino */}
            {tempExercises.length > 0 && (
              <div className="flex flex-col mt-6 p-4 border border-dashed border-blue/30 rounded-md bg-blue/5">
                <h3 className="mb-3 font-semibold text-dark-blue">
                  Novo Treino de {selectedDay} (Pendente)
                </h3>
                {tempExercises.map((ex, i) => (
                  <div
                    key={i}
                    className="flex justify-between items-center p-3 mb-2 rounded border border-slate-200 bg-white/30"
                  >
                    <span>
                      <strong>{ex.exerciseTitle}</strong> | {ex.serie} x{' '}
                      {ex.repetitions}
                    </span>
                    <button
                      onClick={() => removeTempExercise(i)}
                      className="p-2 hover:bg-red-50 rounded-full"
                    >
                      <Image
                        src="/lixo.png"
                        alt="Deletar"
                        width="20"
                        height="20"
                      />
                    </button>
                  </div>
                ))}
                <button
                  onClick={saveFullWorkoutToDatabase}
                  disabled={isSaving}
                  className="self-end bg-dark-blue text-white px-5 py-3 rounded-md font-bold hover:bg-blue"
                >
                  {isSaving
                    ? 'Salvando...'
                    : `Confirmar Treino para ${selectedDay}`}
                </button>
              </div>
            )}

            {/* Exercícios Salvos no Banco */}
            {exerciseSessions.length > 0 && (
              <div className="mt-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {exerciseSessions
                  .filter(
                    (es) =>
                      workoutSessions.find(
                        (ws) =>
                          String(ws.workoutSession_ID) ===
                          String(es.workoutSession_ID),
                      )?.weekDay === selectedDay,
                  )
                  .map((es) => {
                    const ex = exercises.find(
                      (e) => String(e.exercise_id) === es.exercise_ID,
                    );
                    return (
                      <article
                        key={es.exerciseSession_ID}
                        className={`rounded-md border p-4 transition-all ${
                          es.isEditing
                            ? 'border-blue bg-blue/5'
                            : 'border-neutral-200 bg-white/30'
                        }`}
                      >
                        <div className="flex justify-between items-start mb-3">
                          <p className="font-semibold text-black">
                            {ex?.title || 'Exercício'}
                          </p>
                          <div className="flex gap-2">
                            <button
                              onClick={() =>
                                toggleEditMode(es.exerciseSession_ID)
                              }
                            >
                              <Image
                                src="/edit-exercise.png"
                                alt="Editar"
                                width="18"
                                height="18"
                              />
                            </button>
                            <button
                              onClick={() =>
                                deleteExerciseSession(es.exerciseSession_ID)
                              }
                            >
                              <Image
                                src="/lixo.png"
                                alt="Excluir"
                                width="18"
                                height="18"
                              />
                            </button>
                          </div>
                        </div>
                        {es.isEditing ? (
                          <div className="grid grid-cols-2 gap-2 mt-auto">
                            <input
                              type="number"
                              className="border border-blue/40 rounded px-2 py-1 text-sm"
                              value={es.serie}
                              onChange={(e) =>
                                updateExerciseSessionLocal(
                                  es.exerciseSession_ID,
                                  'serie',
                                  e.target.value,
                                )
                              }
                            />
                            <input
                              type="number"
                              className="border border-blue/40 rounded px-2 py-1 text-sm"
                              value={es.repetitions}
                              onChange={(e) =>
                                updateExerciseSessionLocal(
                                  es.exerciseSession_ID,
                                  'repetitions',
                                  e.target.value,
                                )
                              }
                            />
                          </div>
                        ) : (
                          <p className="text-black/60 text-sm mt-auto">
                            Configuração:{' '}
                            <span className="font-medium text-black">
                              {es.serie} x {es.repetitions}
                            </span>
                          </p>
                        )}
                      </article>
                    );
                  })}
              </div>
            )}

            {/* Botão Salvar Edições */}
            {exerciseSessions.some((es) => es.isEditing) && (
              <div className="w-full my-6">
                <button
                  onClick={saveAllWorkoutChanges}
                  disabled={isSaving}
                  className="bg-dark-blue text-white px-4 py-2 rounded-md text-sm font-bold hover:bg-blue"
                >
                  {isSaving ? 'Salvando...' : 'Atualizar Treino Salvo'}
                </button>
              </div>
            )}

            <h2 className="text-xl font-bold mt-12 py-4 border-t border-gray-300">
              Agendamento de Consultas
            </h2>

            <form
              onSubmit={handleSchedule}
              className="grid grid-cols-1 md:grid-cols-12 gap-8"
            >
              {/* Calendário */}
              <div className="md:col-span-7">
                <div className="bg-white/30 h-full p-4 rounded-xl border border-neutral-200">
                  <h3 className="text-lg font-semibold text-black mb-4">
                    Selecionar Data
                  </h3>
                  <Calendar
                    onChange={(d: any) => setSelectedDate(d)}
                    value={selectedDate}
                    locale="pt-BR"
                  />
                </div>
              </div>

              {/* Horário e Descrição */}
              <div className="md:col-span-5 space-y-6">
                <div className="bg-white/30 h-full p-4 rounded-xl border border-neutral-200 space-y-4">
                  <h3 className="text-lg font-semibold text-black mb-4">
                    Detalhes do Horário
                  </h3>

                  <div>
                    <label className="text-sm text-black/60 uppercase">
                      Horário da Consulta
                    </label>
                    <input
                      type="time"
                      className="w-full p-3 border border-neutral-300 rounded-lg text-lg focus:ring-2 focus:ring-blue focus:outline-none"
                      value={selectedTime}
                      onChange={(e) => setSelectedTime(e.target.value)}
                      required
                    />
                  </div>

                  <div>
                    <label className="text-sm text-black/60 uppercase">
                      Descrição/Notas
                    </label>
                    <textarea
                      rows={4}
                      className="w-full p-3 border border-neutral-300 rounded-lg focus:ring-2 focus:ring-blue focus:outline-none"
                      placeholder="Motivo da consulta..."
                      value={description}
                      onChange={(e) => setDescription(e.target.value)}
                    />
                  </div>

                  <button
                    type="submit"
                    disabled={isSavingAppointment}
                    className={`w-full py-4 rounded-lg font-bold text-white transition-all shadow-md ${
                      isSavingAppointment
                        ? 'bg-neutral-400'
                        : 'bg-dark-blue hover:bg-blue'
                    }`}
                  >
                    {isSavingAppointment
                      ? 'Agendando...'
                      : 'Confirmar Agendamento'}
                  </button>
                </div>
              </div>
            </form>
          </>
        )}
      </div>
    </section>
  );
}
