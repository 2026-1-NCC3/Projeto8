/* eslint-disable @typescript-eslint/no-explicit-any */
'use client';

import { useMemo, useState, useEffect } from 'react';
import Select from 'react-select';
import { usePatients, PatientRequest } from '@/app/hooks/useGetPatients';
import { useExercises } from '@/app/hooks/useGetExercises';
import { useGetWorkouts } from '@/app/hooks/useGetWorkouts';

export default function CalendarsPage() {
  const { patients, updatePatient } = usePatients();
  const { exercises } = useExercises();

  const [selectedId, setSelectedId] = useState('');

  const [isMounted, setIsMounted] = useState(false);
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

  if (!isMounted) return null;

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <div className="col-span-4 md:col-span-12 mt-6">
        <h2 className="text-xl font-bold border-t pt-4 border-neutral-200">
          Calendário do Paciente
        </h2>

        {/* Dropdown de pacientes */}
        <div className="mt-4 max-w-md">
          <Select
            instanceId="patient-select-main"
            options={patientOptions}
            placeholder="Selecione um paciente..."
            isClearable
            onChange={(opt) => setSelectedId(opt?.value || '')}
          />
        </div>

        <div className="flex gap-2 mt-4 flex-wrap">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => setSelectedDay(day)}
              className={`py-2 px-4 rounded-md border transition-all ${
                selectedDay === day
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
          <div className="col-span-12 md:col-span-5">
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
                  <strong>{ex.exerciseTitle}</strong> | {ex.serie} x{' '}
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
              {isSaving
                ? 'Gravando...'
                : `Criar novo treino paraa ${selectedDay}`}
            </button>
          </div>
        )}

        {/* Exercícios Salvos */}
        <div className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
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
                        : 'border-neutral-200 bg-white'
                    }`}
                  >
                    <div className="flex flex-col h-full">
                      <div className="flex justify-between items-start mb-3">
                        <p className="font-semibold text-black">
                          {ex?.title || 'Exercício'}
                        </p>
                        <div className="flex gap-2">
                          <button
                            onClick={() =>
                              toggleEditMode(es.exerciseSession_ID)
                            }
                            className={`p-1.5 rounded-md transition-colors hover:bg-light-blue ${
                              es.isEditing
                                ? 'bg-blue text-white'
                                : 'bg-neutral-100'
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
                                  'serie',
                                  e.target.value,
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
                                  'repetitions',
                                  e.target.value,
                                )
                              }
                            />
                          </div>
                        </div>
                      ) : (
                        <p className="text-black/60 text-sm mt-auto">
                          Configuração:{' '}
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

          {exerciseSessions.some(
            (es) =>
              es.isEditing &&
              workoutSessions.find(
                (ws) =>
                  String(ws.workoutSession_ID) === String(es.workoutSession_ID),
              )?.weekDay === selectedDay,
          ) && (
            <div className="w-full my-6">
              <button
                onClick={saveAllWorkoutChanges}
                disabled={isSaving}
                className="bg-dark-blue text-white px-4 py-2 rounded-md text-sm font-bold hover:bg-blue transition-all disabled:opacity-50 shadow-sm"
              >
                {isSaving
                  ? 'Salvando...'
                  : 'Atualizar treino existente de ' + selectedDay}
              </button>
            </div>
          )}
        </div>
      </div>
    </section>
  );
}
