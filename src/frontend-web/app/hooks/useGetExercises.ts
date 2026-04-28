import { useState, useEffect } from 'react';

export type Exercise = {
  exercise_id: number;
  title: string;
  description: string;
  tags: string;
  midiaURL: string;
};

export type ExerciseRequest = Omit<Exercise, 'exercise_id'>;

export function useExercises() {
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [reload, setReload] = useState(false);

  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  // Busca inicial com normalização de ID
  useEffect(() => {
    async function fetchExercises() {
      try {
        const res = await fetch(`${API_URL}/api/exercise/all?page=0&size=100`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        });
        if (res.ok) {
          const data = await res.json();
          // Normaliza exercise_ID (Java) para exercise_id (Frontend)
          const normalizedData = data.map((item: any) => ({
            ...item,
            exercise_id: item.exercise_id || item.exercise_ID,
          }));
          setExercises(normalizedData);
        }
      } catch (error) {
        console.error('Erro ao buscar exercícios:', error);
      }
    }
    fetchExercises();
  }, [API_URL, reload]);

  // Criar Exercício (POST)
  const addExercise = async (newExerciseData: ExerciseRequest) => {
    try {
      const res = await fetch(`${API_URL}/api/exercise/create-exercise`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newExerciseData),
      });

      if (res.ok) {
        setReload((prev) => !prev);
        return true;
      }
      return false;
    } catch (error) {
      console.error('Erro na requisição POST:', error);
      return false;
    }
  };

  // Atualizar Exercício (PUT)
  const updateExercise = async (id: number, updatedData: ExerciseRequest) => {
    try {
      const res = await fetch(`${API_URL}/api/exercise/updateExercise/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedData),
      });

      if (res.ok) {
        setReload((prev) => !prev);
        return true;
      }
      return false;
    } catch (error) {
      return false;
    }
  };

  // Remover Exercício (DELETE)
  const removeExercise = async (id: number) => {
    if (!id) return;
    if (!window.confirm('Deseja excluir este exercício permanentemente?'))
      return;

    try {
      const res = await fetch(
        `${API_URL}/api/exercise/deleteExerciseId/${id}`,
        {
          method: 'DELETE',
        },
      );
      if (res.ok) {
        setExercises((prev) => prev.filter((ex) => ex.exercise_id !== id));
        setReload((prev) => !prev);
      }
    } catch (error) {
      console.error('Erro ao deletar exercício:', error);
    }
  };

  return {
    exercises,
    addExercise,
    removeExercise,
    updateExercise,
  };
}
