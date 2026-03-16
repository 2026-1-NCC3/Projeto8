// src/frontend-web/app/hooks/useExercises.ts
import { useState, useEffect, useMemo } from "react";

export type Exercise = {
  exercise_ID: number;
  title: string;
  description: string;
  tags: string;
  midiaURL: string;
};

export type ExerciseRequest = Omit<Exercise, "exercise_ID">;

export function useExercises() {
  const [exercises, setExercises] = useState<Exercise[]>([]);

  const API_URL = process.env.NEXT_PUBLIC_API_URL;
  const API_USER = process.env.NEXT_PUBLIC_API_USER;
  const API_PASS = process.env.NEXT_PUBLIC_API_PASS;

  // 1. Wrap the auth token in useMemo so it is completely stable
  const basicAuth = useMemo(() => {
    if (typeof window !== "undefined" && API_USER && API_PASS) {
      return "Basic " + btoa(`${API_USER}:${API_PASS}`);
    }
    return "";
  }, [API_USER, API_PASS]);

  // 2. Move the fetch logic INSIDE the useEffect
  useEffect(() => {
    async function fetchExercises() {
      try {
        const res = await fetch(`${API_URL}/api/exercise/all?page=0&size=100`, {
          method: "GET",
          headers: {
            Authorization: basicAuth,
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

    // Only fetch if we have an auth token ready
    if (basicAuth) {
      fetchExercises();
    }
  }, [API_URL, basicAuth]); // Now dependencies are clean and safe

  const addExercise = async (newExerciseData: ExerciseRequest) => {
    try {
      const res = await fetch(`${API_URL}/api/exercise/create-exercise`, {
        method: "POST",
        headers: {
          Authorization: basicAuth,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newExerciseData),
      });

      if (res.ok) {
        const newExercise = await res.json();
        setExercises((prev) => [newExercise, ...prev]);
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
          method: "DELETE",
          headers: {
            Authorization: basicAuth,
          },
        },
      );
      if (res.ok) {
        setExercises((prev) =>
          prev.filter((exercise) => exercise.exercise_ID !== id),
        );
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
