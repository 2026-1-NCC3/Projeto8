/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect, useCallback } from "react";
import { Exercise } from "./useGetExercises";

const dayMapping: { [key: string]: string } = {
  Segunda: "SEG",
  Terça: "TER",
  Quarta: "QUA",
  Quinta: "QUI",
  Sexta: "SEX",
  Sábado: "SAB",
  Domingo: "DOM",
};

const reverseDayMapping: { [key: string]: string } = Object.fromEntries(
  Object.entries(dayMapping).map(([key, value]) => [value, key]),
);

const daysOfWeek = Object.keys(dayMapping);

export type WorkoutSession = {
  workoutSession_ID: string;
  patient_ID: string;
  weekDay: string;
};

export type ExerciseSession = {
  exerciseSession_ID: string;
  workoutSession_ID: string;
  patient_ID: string;
  exercise_ID: string;
  serie: string;
};

export function useGetWorkouts(selectedPatient: any, exercises: Exercise[]) {
  const [workoutSessions, setWorkoutSessions] = useState<WorkoutSession[]>([]);
  const [exerciseSessions, setExerciseSessions] = useState<ExerciseSession[]>(
    [],
  );
  const [tempExercises, setTempExercises] = useState<any[]>([]);
  const [selectedDay, setSelectedDay] = useState<string>(daysOfWeek[0]);
  const [isSaving, setIsSaving] = useState(false);
  const [scheduleForm, setScheduleForm] = useState({
    exerciseName: "",
    serie: "",
    repetitions: "",
  });

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  const fetchWorkouts = useCallback(async () => {
    if (!selectedPatient) return;
    const patientId = String(
      selectedPatient.patient_id || selectedPatient.patient_ID,
    );
    const token =
      typeof window !== "undefined" ? localStorage.getItem("token") : "";

    try {
      const res = await fetch(`${API_URL}/api/workout/patient/${patientId}`, {
        headers: { Authorization: token ? `Bearer ${token}` : "" },
      });
      if (res.ok) {
        const data = await res.json();
        const loadedWS: WorkoutSession[] = [];
        const loadedES: ExerciseSession[] = [];

        data.forEach((ws: any) => {
          loadedWS.push({
            workoutSession_ID: String(ws.workoutSession_id),
            patient_ID: patientId,
            weekDay: reverseDayMapping[ws.weekDay] || ws.weekDay,
          });

          ws.exercises?.forEach((ex: any) => {
            loadedES.push({
              exerciseSession_ID: String(ex.exercisesession_id),
              workoutSession_ID: String(ws.workoutSession_id),
              patient_ID: patientId,
              exercise_ID: String(ex.exercise?.exercise_id || ex.exercise_id),
              serie: `${ex.serie} x ${ex.repetitions}`,
            });
          });
        });
        setWorkoutSessions(loadedWS);
        setExerciseSessions(loadedES);
      }
    } catch (e) {
      console.error(e);
    }
  }, [selectedPatient, API_URL]);

  useEffect(() => {
    fetchWorkouts();
  }, [fetchWorkouts]);

  function addExerciseToTempList(event: React.FormEvent) {
    event.preventDefault();
    if (
      !scheduleForm.exerciseName ||
      !scheduleForm.serie ||
      !scheduleForm.repetitions
    )
      return;
    const match = exercises.find(
      (e) => String(e.exercise_id) === scheduleForm.exerciseName,
    );
    setTempExercises((prev) => [
      ...prev,
      {
        exercise_id: parseInt(scheduleForm.exerciseName),
        exerciseTitle: match?.title || "Exercício",
        serie: parseInt(scheduleForm.serie),
        repetitions: parseInt(scheduleForm.repetitions),
      },
    ]);
    setScheduleForm({
      ...scheduleForm,
      exerciseName: "",
      serie: "",
      repetitions: "",
    });
  }

  async function saveFullWorkoutToDatabase() {
    if (!selectedPatient || tempExercises.length === 0) return;
    setIsSaving(true);
    const patientId = String(
      selectedPatient.patient_id || selectedPatient.patient_ID,
    );
    const token =
      typeof window !== "undefined" ? localStorage.getItem("token") : "";

    try {
      const workoutRes = await fetch(`${API_URL}/api/workout/create-workout`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: token ? `Bearer ${token}` : "",
        },
        body: JSON.stringify({
          weekDay: dayMapping[selectedDay],
          checked: false,
          patient_id: patientId,
        }),
      });

      if (!workoutRes.ok) throw new Error();
      const workoutData = await workoutRes.json();

      await Promise.all(
        tempExercises.map((ex) =>
          fetch(`${API_URL}/api/exerciseSession`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: token ? `Bearer ${token}` : "",
            },
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
      fetchWorkouts();
      alert("Salvo com sucesso!");
    } catch (e) {
      alert("Erro ao salvar. Verifique o console.");
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
    addExerciseToTempList,
    removeTempExercise: (i: number) =>
      setTempExercises((prev) => prev.filter((_, idx) => idx !== i)),
    saveFullWorkoutToDatabase,
  };
}
