/* eslint-disable @typescript-eslint/no-explicit-any */
'use client';

import { useMemo, useState } from 'react';
import Image from 'next/image';
import dynamic from 'next/dynamic';

import { usePatients } from '@/app/hooks/useGetPatients';
import { useExercises } from '@/app/hooks/useGetExercises';
import { useGetWorkouts } from '@/app/hooks/useGetWorkouts';
import { useAppointments } from '@/app/hooks/useAppointments';

const Select = dynamic(() => import('react-select'), { ssr: false });
const Calendar = dynamic(() => import('react-calendar'), { ssr: false });

import 'react-calendar/dist/Calendar.css';

const calendarStyles = `
  .react-calendar { 
    width: 100%; 
    border: none; 
    font-family: inherit; 
    border-radius: 0.375rem; 
    background: transparent;
  }

  /* Aplica cantos arredondados */
  .react-calendar__tile {
    border-radius: 0.375rem;
    transition: background-color 0.2s, color 0.2s;
    padding: 12px 6.6667px;
  }

  /* Hover arredondado nos dias */
  .react-calendar__tile:enabled:hover,
  .react-calendar__tile:enabled:focus {
    border-radius: 0.375rem !important;
    background-color: #e6e6e6;
  }
  
  /* Dia selecionado */
  .react-calendar__tile--active { 
    background: #FFB347 !important; 
    border-radius: 0.375rem; 
    color: white !important; 
  }
  
  /* Dia atual */
  .react-calendar__tile--now { 
    background: #ffc16a !important; 
    color: white !important; 
    border-radius: 0.375rem;
    font-weight: bold;
  }

  /* Botões de navegação (mês anterior/próximo) arredondados */
  .react-calendar__navigation button {
    border-radius: 0.375rem;
    min-width: 44px;
    background: none;
    font-size: 1.1rem; 
    font-weight: bold; 
  }

  .react-calendar__navigation button:enabled:hover,
  .react-calendar__navigation button:enabled:focus {
    background-color: #e6e6e6;
    border-radius: 0.375rem !important;
  }

  /* Cabeçalho dos dias da semana */
  .react-calendar__month-view__weekdays__weekday {
    text-transform: uppercase;
    font-size: 0.75rem;
    font-weight: bold;
    color: #000000;
    text-decoration: none;
  }

  .react-calendar__month-view__weekdays__weekday--weekend abbr {
    color: #000000 !important;
  }

  .react-calendar__month-view__days__day--weekend {
    color: #000000 !important;
  }

  /* Marcador de Consulta (Bolinha) */
  .has-appointment {
    position: relative;
    font-weight: bold;
  }

  .has-appointment::after {
    content: '';
    position: absolute;
    bottom: 15%;
    left: 50%;
    transform: translateX(-50%);
    width: 6px;
    height: 6px;
    background-color: #009CB4;
    border-radius: 50%;
  }

  /* Garante que o ponto apareça mesmo no dia selecionado */
  .react-calendar__tile--active.has-appointment::after {
    background-color: white;
  }
`;

export default function CalendarsPage() {
  const { patients } = usePatients();
  const { exercises } = useExercises();

  const [selectedId, setSelectedId] = useState('');
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [selectedTime, setSelectedTime] = useState('');
  const [description, setDescription] = useState('');

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

  const { createAppointment, isSavingAppointment, appointments } =
    useAppointments(selectedId);

  const appointmentOnSelectedDate = useMemo(() => {
    const dateString = selectedDate.toISOString().split('T')[0];
    return appointments.find((app) => app.date.split('T')[0] === dateString);
  }, [appointments, selectedDate]);

  const getTileClassName = ({ date, view }: any) => {
    if (view === 'month') {
      const dateString = date.toISOString().split('T')[0];
      const hasApp = appointments.some(
        (app) => app.date.split('T')[0] === dateString,
      );
      return hasApp ? 'has-appointment' : '';
    }
    return '';
  };

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

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12 pb-20 p-6">
      <style>{calendarStyles}</style>

      <h1 className="font-display text-4xl text-neutral-900 col-span-full pt-6">
        Gerenciar Calendários e Consultas
      </h1>

      <div className="col-span-4 md:col-span-12">
        <h2 className="text-xl font-bold py-4">
          Selecione o Paciente
        </h2>

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
            className='bg-white/20!'
          />
        </div>

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
                      ? 'bg-salmon text-white border-salmon shadow-sm'
                      : 'bg-white border-neutral-200 hover:bg-neutral-50'
                  }`}
                >
                  {day}
                </button>
              ))}
            </div>

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
                className="col-span-6 md:col-span-2 px-3 py-1 border border-black/40 rounded-md"
                value={scheduleForm.serie}
                onChange={(e) =>
                  setScheduleForm((prev) => ({ ...prev, serie: e.target.value }))
                }
                required
              />
              <input
                type="number"
                placeholder="Reps"
                className="col-span-6 md:col-span-3 px-3 py-1 border border-black/40 rounded-md"
                value={scheduleForm.repetitions}
                onChange={(e) =>
                  setScheduleForm((prev) => ({ ...prev, repetitions: e.target.value }))
                }
                required
              />
              <button
                type="submit"
                className="col-span-12 md:col-span-3 bg-black text-white rounded-md font-bold hover:bg-black/80 transition-colors"
              >
                + Adicionar Exercício
              </button>
            </form>

            {/* Lista Temporária */}
            {tempExercises.length > 0 && (
              <div className="flex flex-col mt-6 p-4 border border-dashed border-blue/30 rounded-md bg-blue/5">
                <h3 className="mb-3 font-semibold text-dark-blue">Treino de {selectedDay} (Temporário)</h3>
                {tempExercises.map((ex, i) => (
                  <div key={i} className="flex justify-between items-center p-3 mb-2 rounded border border-slate-200 bg-white/30">
                    <span><strong>{ex.exerciseTitle}</strong> | {ex.serie} x {ex.repetitions}</span>
                    <button onClick={() => removeTempExercise(i)} className="p-2 hover:bg-salmon bg-salmon/50 rounded-full">
                      <Image src="/lixo.png" alt="Deletar" className='group-hover:invert' width={20} height={20} />
                    </button>
                  </div>
                ))}
                <button onClick={saveFullWorkoutToDatabase} disabled={isSaving} className="self-end bg-dark-blue text-white px-5 py-3 rounded-md font-bold hover:bg-blue">
                  {isSaving ? 'Salvando...' : `Confirmar Treino para ${selectedDay}`}
                </button>
              </div>
            )}

            {/* Exercícios Salvos */}
            <div className="mt-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {exerciseSessions
                .filter((es) => workoutSessions.find((ws) => String(ws.workoutSession_ID) === String(es.workoutSession_ID))?.weekDay === selectedDay)
                .map((es) => {
                  const ex = exercises.find((e) => String(e.exercise_id) === es.exercise_ID);
                  return (
                    <article key={es.exerciseSession_ID} className={`rounded-md border p-4 transition-all ${es.isEditing ? 'border-blue bg-blue/5' : 'border-neutral-200 bg-white/30'}`}>
                      <div className="flex justify-between items-start mb-3">
                        <p className="font-semibold text-black">{ex?.title || 'Exercício'}</p>
                        <div className="flex gap-2">
                          <button onClick={() => toggleEditMode(es.exerciseSession_ID)}>
                            <Image src="/edit-exercise.png" alt="Editar" width={18} height={18} />
                          </button>
                          <button onClick={() => deleteExerciseSession(es.exerciseSession_ID)}>
                            <Image src="/lixo.png" alt="Excluir" width={18} height={18} />
                          </button>
                        </div>
                      </div>
                      {es.isEditing ? (
                        <div className="grid grid-cols-2 gap-2 mt-auto">
                          <input type="number" className="border border-blue/40 rounded px-2 py-1 text-sm" value={es.serie} onChange={(e) => updateExerciseSessionLocal(es.exerciseSession_ID, 'serie', e.target.value)} />
                          <input type="number" className="border border-blue/40 rounded px-2 py-1 text-sm" value={es.repetitions} onChange={(e) => updateExerciseSessionLocal(es.exerciseSession_ID, 'repetitions', e.target.value)} />
                        </div>
                      ) : (
                        <p className="text-black/60 text-sm mt-auto">Configuração: <span className="font-medium text-black">{es.serie} x {es.repetitions}</span></p>
                      )}
                    </article>
                  );
                })}
            </div>

            <h2 className="text-xl font-bold mt-12 py-4 border-t border-gray-300">
              Agendamento de Consultas
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-12 gap-8">
              <div className="md:col-span-7">
                <div className="bg-white/30 h-fit p-6 rounded-xl border border-neutral-200">
                  <h3 className="text-lg font-semibold text-black mb-4">Selecionar Data</h3>
                  <Calendar
                    onChange={(d: any) => setSelectedDate(d)}
                    value={selectedDate}
                    locale="pt-BR"
                    tileClassName={getTileClassName}
                  />
                </div>

                <div className="mt-4 p-4 bg-blue/10 border border-blue-200 rounded-lg">
                  <div className="flex items-center gap-2 mb-2">
                    {appointmentOnSelectedDate && (
                      <span className="bg-dark-blue text-white text-sm font-bold px-2 py-0.5 rounded">CONSULTA</span>
                    )}
                    <span className="text-dark-blue font-bold">
                      {appointmentOnSelectedDate?.time || 'Não há consultas marcadas nesta data.'}
                    </span>
                  </div>
                  {appointmentOnSelectedDate && (
                    <p className="text-sm text-black/80">{appointmentOnSelectedDate.description || 'Sem observações.'}</p>
                  )}
                </div>
              </div>

              <div className="md:col-span-5">
                <form onSubmit={handleSchedule} className="bg-white/30 h-full p-6 rounded-xl border border-neutral-200 space-y-4">
                  <h3 className="text-lg font-semibold text-black mb-4">Novo Agendamento</h3>
                  <div>
                    <label className="text-sm text-black/60 uppercase font-bold">Horário</label>
                    <input type="time" className="w-full p-3 border border-neutral-300 rounded-lg text-lg focus:ring-2 focus:ring-blue focus:outline-none" value={selectedTime} onChange={(e) => setSelectedTime(e.target.value)} required />
                  </div>
                  <div>
                    <label className="text-sm text-black/60 uppercase font-bold">Descrição</label>
                    <textarea rows={4} className="w-full p-3 border border-neutral-300 rounded-lg focus:ring-2 focus:ring-blue focus:outline-none" placeholder="Motivo da consulta..." value={description} onChange={(e) => setDescription(e.target.value)} />
                  </div>
                  <button type="submit" disabled={isSavingAppointment} className={`w-full py-4 rounded-lg font-bold text-white transition-all shadow-md ${isSavingAppointment ? 'bg-black/40' : 'bg-black hover:bg-black/80'}`}>
                    {isSavingAppointment ? 'Agendando...' : 'Confirmar Agendamento'}
                  </button>
                </form>
              </div>
            </div>
          </>
        )}
      </div>
    </section>
  );
}