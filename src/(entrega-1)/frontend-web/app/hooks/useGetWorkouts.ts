/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect, useCallback } from 'react';
import { Exercise } from './useGetExercises';

const dayMapping: { [key: string]: string } = {
  Segunda: 'SEG',
  Terça: 'TER',
  Quarta: 'QUA',
  Quinta: 'QUI',
  Sexta: 'SEX',
  Sábado: 'SAB',
  Domingo: 'DOM',
};

const reverseDayMapping: { [key: string]: string } = Object.fromEntries(
  Object.entries(dayMapping).map(([key, value]) => [value, key]),
);

const daysOfWeek = Object.keys(dayMapping);

export function useGetWorkouts(selectedPatient: any, exercises: Exercise[]) {
  const [workoutSessions, setWorkoutSessions] = useState<any[]>([]);
  const [exerciseSessions, setExerciseSessions] = useState<any[]>([]);
  const [tempExercises, setTempExercises] = useState<any[]>([]);
  const [selectedDay, setSelectedDay] = useState<string>(daysOfWeek[0]);
  const [isSaving, setIsSaving] = useState(false);
  const [scheduleForm, setScheduleForm] = useState({
    exerciseName: '',
    serie: '',
    repetitions: '',
  });

  const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  const fetchWorkouts = useCallback(async () => {
    if (!selectedPatient) return;
    const patientId = String(
      selectedPatient.patient_id || selectedPatient.patient_ID,
    );

    try {
      const res = await fetch(`${API_URL}/api/workout/patient/${patientId}`);
      if (res.ok) {
        const data = await res.json();
        const loadedWS: any[] = [];
        const loadedES: any[] = [];

        data.forEach((ws: any) => {
          loadedWS.push({
            workoutSession_ID: String(ws.workoutSession_id),
            weekDay: reverseDayMapping[ws.weekDay] || ws.weekDay,
          });

          ws.exercises?.forEach((ex: any) => {
            loadedES.push({
              exerciseSession_ID: String(ex.exercisesession_id),
              workoutSession_ID: String(ws.workoutSession_id),
              exercise_ID: String(
                ex.exercise?.exercise_id || ex.exercise || '',
              ),
              serie: String(ex.serie),
              repetitions: String(ex.repetitions),
              isEditing: false,
            });
          });
        });

        setWorkoutSessions(loadedWS);
        setExerciseSessions(loadedES);
      }
    } catch (e) {
      console.error('Erro ao buscar treinos:', e);
    }
  }, [selectedPatient, API_URL]);

  useEffect(() => {
    fetchWorkouts();
  }, [fetchWorkouts]);

  const toggleEditMode = (id: string) => {
    setExerciseSessions((prev) =>
      prev.map((es) =>
        es.exerciseSession_ID === id ? { ...es, isEditing: !es.isEditing } : es,
      ),
    );
  };

  const updateExerciseSessionLocal = (
    id: string,
    field: 'serie' | 'repetitions',
    value: string,
  ) => {
    setExerciseSessions((prev) =>
      prev.map((es) =>
        es.exerciseSession_ID === id ? { ...es, [field]: value } : es,
      ),
    );
  };

  const deleteExerciseSession = async (id: string) => {
    if (!window.confirm('Deseja realmente excluir este exercício deste dia?'))
      return;

    try {
      const res = await fetch(
        `${API_URL}/api/exerciseSession/deleteExerciseSession/${id}`,
        { method: 'DELETE' },
      );
      if (res.ok) {
        setExerciseSessions((prev) =>
          prev.filter((es) => es.exerciseSession_ID !== id),
        );
      }
    } catch (e) {
      console.error(e);
    }
  };

  const saveAllWorkoutChanges = async () => {
    if (!selectedPatient) return;
    setIsSaving(true);
    const patientId = selectedPatient.patient_id || selectedPatient.patient_ID;

    const toUpdate = exerciseSessions.filter((es) => {
      const ws = workoutSessions.find(
        (w) => w.workoutSession_ID === es.workoutSession_ID,
      );
      return ws?.weekDay === selectedDay;
    });

    try {
      await Promise.all(
        toUpdate.map((es) =>
          fetch(
            `${API_URL}/api/exerciseSession/updateExerciseSession/${es.exerciseSession_ID}`,
            {
              method: 'PUT',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                exercise_id: parseInt(es.exercise_ID),
                workoutSession: parseInt(es.workoutSession_ID),
                patient_id: patientId,
                serie: parseInt(es.serie),
                repetitions: parseInt(es.repetitions),
                feelPain: false,
              }),
            },
          ),
        ),
      );
      alert(`Treino de ${selectedDay} atualizado!`);
      setExerciseSessions((prev) =>
        prev.map((es) => ({ ...es, isEditing: false })),
      );
      fetchWorkouts();
    } catch (e) {
      alert('Erro ao salvar alterações.');
    } finally {
      setIsSaving(false);
    }
  };

  const addExerciseToTempList = (e: any) => {
    e.preventDefault();
    if (!scheduleForm.exerciseName) return;
    const match = exercises.find(
      (ex) => String(ex.exercise_id) === scheduleForm.exerciseName,
    );
    setTempExercises((prev) => [
      ...prev,
      {
        exercise_id: parseInt(scheduleForm.exerciseName),
        exerciseTitle: match?.title || 'Exercício',
        serie: parseInt(scheduleForm.serie),
        repetitions: parseInt(scheduleForm.repetitions),
      },
    ]);
    setScheduleForm({ exerciseName: '', serie: '', repetitions: '' });
  };

  async function saveFullWorkoutToDatabase() {
    if (!selectedPatient || tempExercises.length === 0) return;
    setIsSaving(true);
    const patientId = String(
      selectedPatient.patient_id || selectedPatient.patient_ID,
    );
    try {
      const workoutRes = await fetch(`${API_URL}/api/workout/create-workout`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          weekDay: dayMapping[selectedDay],
          checked: false,
          patient_id: patientId,
        }),
      });
      const workoutData = await workoutRes.json();
      await Promise.all(
        tempExercises.map((ex) =>
          fetch(`${API_URL}/api/exerciseSession/createExerciseSession`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              exercise_id: ex.exercise_id,
              workoutSession: workoutData.workoutSession_id,
              patient_id: patientId,
              serie: ex.serie,
              repetitions: ex.repetitions,
              feelPain: false,
            }),
          }),
        ),
      );
      setTempExercises([]);
      await fetchWorkouts();
    } catch (e) {
      console.error(e);
    } finally {
      setIsSaving(false);
    }
  }

  return {
    daysOfWeek,
    workoutSessions,
    exerciseSessions,
    tempExercises,
    scheduleForm,
    selectedDay,
    isSaving,
    setScheduleForm,
    setSelectedDay,
    updateExerciseSessionLocal,
    deleteExerciseSession,
    saveAllWorkoutChanges,
    toggleEditMode,
    addExerciseToTempList,
    saveFullWorkoutToDatabase,
    removeTempExercise: (i: number) =>
      setTempExercises((prev) => prev.filter((_, idx) => idx !== i)),
  };
}
