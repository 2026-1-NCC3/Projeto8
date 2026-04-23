'use client';

import { useMemo, useState, useEffect } from 'react';
import Select from 'react-select';
import Calendar from 'react-calendar';
import Image from 'next/image';

import { usePatients } from '@/app/hooks/useGetPatients';
import { useExercises } from '@/app/hooks/useGetExercises';
import { useGetWorkouts } from '@/app/hooks/useGetWorkouts';
import { useAppointments } from '@/app/hooks/useAppointments';

import 'react-calendar/dist/Calendar.css';

const calendarStyles = `
  .react-calendar { width: 100%; border: none; font-family: inherit; }
  .react-calendar__tile--active { background: #0070f3 !important; border-radius: 8px; }
  .react-calendar__navigation button { font-size: 1.2rem; font-weight: bold; }
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
    return patients.find((p) => String(p.patient_id) === String(selectedId)) || null;
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
      alert("Selecione um paciente e um horário.");
      return;
    }

    const datePart = selectedDate.toISOString().split('T')[0];
    const isoDateTime = `${datePart}T00:00:00`;

    const success = await createAppointment({
      date: isoDateTime,
      time: selectedTime,
      patient_id: String(selectedPatient.patient_id),
      description: description
    });

    if (success) {
      setSelectedTime('');
      setDescription('');
    }
  };

  if (!isMounted) return null;

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12 p-6">
      <style>{calendarStyles}</style>

      <h1 className="font-display text-4xl text-neutral-900 col-span-full pt-6">
        Gerenciar Calendários
      </h1>

      <div className="col-span-4 md:col-span-12">
        <h2 className="text-xl font-bold pt-4">Selecione o Paciente</h2>

        <div className="mt-4 max-w-md">
          <Select
            instanceId="patient-select-main"
            options={patientOptions}
            placeholder="Selecione um paciente..."
            isClearable
            onChange={(opt) => setSelectedId(opt?.value || '')}
          />
        </div>

        <h2 className="text-xl font-bold mt-14 pt-4 border-t border-gray-300">
          Calendário do Paciente Selecionado
        </h2>

        <div className="flex gap-2 mt-4 flex-wrap">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-md border transition-all ${selectedDay === day
                ? 'bg-blue text-white border-blue'
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
          className="mt-6 grid grid-cols-12 gap-3 p-4 rounded-md border border-neutral-200"
        >
          <div className="col-span-12 md:col-span-4">
            <Select
              instanceId="exercise-select-field"
              options={exercises.map((e) => ({
                value: String(e.exercise_id),
                label: e.title,
              }))}
              onChange={(opt) =>
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
            className="col-span-6 md:col-span-2 px-3 py-1 border border-black/20 rounded-md"
            value={scheduleForm.serie}
            onChange={(e) => setScheduleForm((prev) => ({ ...prev, serie: e.target.value }))}
            required
          />
          <input
            type="number"
            placeholder="Repetições"
            className="col-span-6 md:col-span-3 px-3 py-1 border border-black/20 rounded-md"
            value={scheduleForm.repetitions}
            onChange={(e) => setScheduleForm((prev) => ({ ...prev, repetitions: e.target.value }))}
            required
          />
          <button
            type="submit"
            className="col-span-12 md:col-span-3 bg-dark-blue text-white rounded-md font-bold hover:bg-blue transition-colors"
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
              <div key={i} className="flex justify-between items-center p-3 mb-2 rounded border border-slate-200">
                <span>
                  <strong>{ex.exerciseTitle}</strong> | {ex.serie} x {ex.repetitions}
                </span>
                <button
                  onClick={() => removeTempExercise(i)}
                  className="flex items-center justify-center rounded-full hover:bg-light-blue p-2"
                >
                  <Image src="/lixo.png" alt="Deletar" width={20} height={20} />
                </button>
              </div>
            ))}
            <button
              onClick={saveFullWorkoutToDatabase}
              disabled={isSaving}
              className="self-end bg-dark-blue text-white px-5 py-3 rounded-md font-bold disabled:opacity-50"
            >
              {isSaving ? 'Salvando...' : `Criar novo treino para ${selectedDay}`}
            </button>
          </div>
        )}

        {/* Exercícios Salvos */}
        <div className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {exerciseSessions
              .filter((es) =>
                workoutSessions.find((ws) => String(ws.workoutSession_ID) === String(es.workoutSession_ID))?.weekDay === selectedDay
              )
              .map((es) => {
                const ex = exercises.find((e) => String(e.exercise_id) === es.exercise_ID);
                return (
                  <article
                    key={es.exerciseSession_ID}
                    className={`rounded-md border p-4 transition-all ${es.isEditing ? 'border-blue bg-blue/5' : 'border-neutral-200 bg-white'
                      }`}
                  >
                    <div className="flex flex-col h-full">
                      <div className="flex justify-between items-start mb-3">
                        <p className="font-semibold text-black">{ex?.title || 'Exercício'}</p>
                        <div className="flex gap-2">
                          <button
                            onClick={() => toggleEditMode(es.exerciseSession_ID)}
                            className={`p-1.5 rounded-md transition-colors ${es.isEditing ? 'bg-blue text-white' : 'bg-neutral-100'}`}
                          >
                            <Image src="/edit-exercise.png" alt="Editar" width={20} height={20} />
                          </button>
                          <button
                            onClick={() => deleteExerciseSession(es.exerciseSession_ID)}
                            className="p-1.5 bg-neutral-100 rounded-md hover:bg-red-100"
                          >
                            <Image src="/lixo.png" alt="Deletar" width={20} height={20} />
                          </button>
                        </div>
                      </div>

                      {es.isEditing ? (
                        <div className="grid grid-cols-2 gap-2 mt-auto">
                          <div>
                            <label className="text-sm text-black/80">Séries</label>
                            <input
                              type="number"
                              className="w-full border border-blue/40 rounded px-2 py-2 text-sm"
                              value={es.serie}
                              onChange={(e) => updateExerciseSessionLocal(es.exerciseSession_ID, 'serie', e.target.value)}
                            />
                          </div>
                          <div>
                            <label className="text-sm text-black/80">Reps</label>
                            <input
                              type="number"
                              className="w-full border border-blue/40 rounded px-2 py-2 text-sm"
                              value={es.repetitions}
                              onChange={(e) => updateExerciseSessionLocal(es.exerciseSession_ID, 'repetitions', e.target.value)}
                            />
                          </div>
                        </div>
                      ) : (
                        <p className="text-black/60 text-sm mt-auto">
                          Configuração: <span className="font-medium text-black">{es.serie} x {es.repetitions}</span>
                        </p>
                      )}
                    </div>
                  </article>
                );
              })}
          </div>

          {exerciseSessions.some(es =>
            es.isEditing && workoutSessions.find(ws => String(ws.workoutSession_ID) === String(es.workoutSession_ID))?.weekDay === selectedDay
          ) && (
              <div className="w-full my-6">
                <button
                  onClick={saveAllWorkoutChanges}
                  disabled={isSaving}
                  className="bg-dark-blue text-white px-4 py-2 rounded-md text-sm font-bold hover:bg-blue disabled:opacity-50 shadow-sm"
                >
                  {isSaving ? 'Salvando...' : `Atualizar treino de ${selectedDay}`}
                </button>
              </div>
            )}
        </div>
      </div>

      {/* Agendamento de consultas */}
      <div className="col-span-full mt-16 pt-8 border-t border-gray-300">
        <h1 className="font-display text-4xl text-neutral-900 col-span-full py-6">
          Novo Agendamento
        </h1>

        <form onSubmit={handleSchedule} className="grid grid-cols-1 md:grid-cols-12 gap-8">
          <div className="md:col-span-7 space-y-6">
            <div className="bg-white p-4 rounded-xl border border-neutral-200 shadow-sm">
              <h3 className='text-xl font-semibold'>Selecione a Data</h3>
              <Calendar
                onChange={(d: any) => setSelectedDate(d)}
                value={selectedDate}
                locale="pt-BR"
              />

              <h3 className='text-xl font-semibold mt-12 pb-4'>Horário da Consulta</h3>
              <input
                type="time"
                className="w-full p-3 border border-neutral-300 rounded-lg text-lg focus:ring-2 focus:ring-blue focus:outline-none"
                value={selectedTime}
                onChange={(e) => setSelectedTime(e.target.value)}
                required
              />
            </div>
          </div>

          <div className="md:col-span-5 space-y-6">
            <div className="bg-white p-6 rounded-xl border border-neutral-200 shadow-sm">
              <h3 className='text-xl font-semibold'>Informações Adicionais</h3>
              <div className="space-y-4">
                <p>
                  <strong>Paciente:</strong> {selectedPatient ? `${selectedPatient.name} ${selectedPatient.surname}` : 'Nenhum'}
                </p>
                <div>
                  <label className="block mb-1"><strong>Comentário:</strong></label>
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
                  className={`w-full py-4 rounded-lg font-bold text-white transition-all shadow-md ${isSavingAppointment ? 'bg-neutral-400' : 'bg-dark-blue hover:bg-blue'
                    }`}
                >
                  {isSavingAppointment ? 'Processando...' : 'Confirmar Agendamento'}
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </section>
  );
}
