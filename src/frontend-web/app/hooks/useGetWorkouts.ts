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

export function useGetWorkouts(selectedPatient: any, exercises: Exercise[]) {
  const [workoutSessions, setWorkoutSessions] = useState<any[]>([]);
  const [exerciseSessions, setExerciseSessions] = useState<any[]>([]);
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
        const data = await res.json(); // Recebe List<WorkoutSesResponseDTO>
        const loadedWS: any[] = [];
        const loadedES: any[] = [];

        data.forEach((ws: any) => {
          loadedWS.push({
            workoutSession_ID: String(ws.workoutSession_id),
            patient_ID: patientId,
            weekDay: reverseDayMapping[ws.weekDay] || ws.weekDay,
          });

          // No seu DTO Java: List<ExerciseSessionResponseDTO> exercises
          ws.exercises?.forEach((ex: any) => {
            loadedES.push({
              exerciseSession_ID: String(ex.exercisesession_id),
              workoutSession_ID: String(ws.workoutSession_id),
              patient_ID: patientId,
              // No seu DTO: ex.exercise.exercise_id
              exercise_ID: String(ex.exercise?.exercise_id || ""),
              serie: `${ex.serie} x ${ex.repetitions}`,
            });
          });
        });

        setWorkoutSessions(loadedWS);
        setExerciseSessions(loadedES);
      }
    } catch (e) {
      console.error("Erro ao buscar treinos:", e);
    }
  }, [selectedPatient, API_URL]);

  useEffect(() => {
    fetchWorkouts();
  }, [fetchWorkouts]);

  async function saveFullWorkoutToDatabase() {
    if (!selectedPatient || tempExercises.length === 0) return;
    setIsSaving(true);
    const patientId = String(
      selectedPatient.patient_id || selectedPatient.patient_ID,
    );
    const token =
      typeof window !== "undefined" ? localStorage.getItem("token") : "";

    try {
      // 1. Criar Workout
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

      const workoutData = await workoutRes.json();
      const newWorkoutId = workoutData.workoutSession_id;

      // 2. Criar Exercise Sessions (URL CORRIGIDA conforme seu Controller)
      await Promise.all(
        tempExercises.map((ex) =>
          fetch(`${API_URL}/api/exerciseSession/createExerciseSession`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: token ? `Bearer ${token}` : "",
            },
            body: JSON.stringify({
              exercise_id: ex.exercise_id,
              workoutSession: newWorkoutId,
              patient_id: patientId,
              serie: ex.serie,
              repetitions: ex.repetitions,
              feelPain: false,
            }),
          }),
        ),
      );

      setTempExercises([]);
      await fetchWorkouts(); // Recarrega do banco
      alert("Treino salvo com sucesso!");
    } catch (e) {
      console.error(e);
      alert("Erro ao salvar no banco.");
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
    addExerciseToTempList: (e: any) => {
      e.preventDefault();
      const match = exercises.find(
        (ex) => String(ex.exercise_id) === scheduleForm.exerciseName,
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
    },
    removeTempExercise: (i: number) =>
      setTempExercises((prev) => prev.filter((_, idx) => idx !== i)),
    saveFullWorkoutToDatabase,
  };
}
