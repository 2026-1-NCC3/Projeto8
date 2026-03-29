import { useState, useEffect, useMemo } from "react";

export type Exercise = {
  exercise_id: number;
  title: string;
  description: string;
  tags: string;
  midiaURL: string;
};

export type ExerciseRequest = Omit<Exercise, "exercise_id">;

export function useExercises() {
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [reload, setReload] = useState(false);

  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    async function fetchExercises() {
      try {
        const res = await fetch(`${API_URL}/api/exercise/all?page=0&size=100`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });
        if (res.ok) {
          const data = await res.json();
          setExercises(data);
        }
      } catch (error) {
        console.error("Erro ao buscar exercícios:", error);
      }
    }

    fetchExercises();

  }, [API_URL, reload]);

  const addExercise = async (newExerciseData: ExerciseRequest) => {
    try {
      const res = await fetch(`${API_URL}/api/exercise/create-exercise`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newExerciseData),
      });

      if (res.ok) {
        const data = await res.json();

        const newExercise: Exercise = {
          ...data,
          exercise_id: data.exercise_id || data.exercise_ID,
        };

        setExercises((prev) => [newExercise, ...prev]);
        setReload(prev => !prev);
        return true;
      } else {
        alert("Erro ao criar exercício.");
        return false;
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
      return false;
    }
  };

  const removeExercise = async (id: number) => {
    try {
      const res = await fetch(
        `${API_URL}/api/exercise/deleteExerciseId/${id}`,
        {
          method: "DELETE"
        },
      );
      if (res.ok) {
        setExercises((prev) =>
          prev.filter((exercise) => exercise.exercise_id !== id),
        );
        setReload(prev => !prev);
      }
    } catch (error) {
      console.error("Erro ao deletar exercício:", error);
    }
  };

  return {
    exercises,
    addExercise,
    removeExercise,
  };
}
