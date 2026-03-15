"use client";

import { useMemo, useState, useEffect } from "react";

type Exercise = {
  exercise_ID: number;
  title: string;
  description: string;
  tags: string;
  midiaURL: string;
};

type ExerciseForm = {
  title: string;
  description: string;
  tags: string;
  midiaURL: string;
};

const emptyForm: ExerciseForm = {
  title: "",
  description: "",
  tags: "",
  midiaURL: "",
};

export default function ExercisesPage() {
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState<ExerciseForm>(emptyForm);

  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  // Fetch exercises on component mount
  useEffect(() => {
    async function fetchExercises() {
      try {
        // Fetching with a large size to get all at once for the list
        const res = await fetch(`${API_URL}/api/exercise/all?page=0&size=100`);
        if (res.ok) {
          const data = await res.json();
          setExercises(data);
        }
      } catch (error) {
        console.error("Erro ao buscar exercícios:", error);
      }
    }
    fetchExercises();
  }, [API_URL]);

  const filtered = useMemo(() => {
    return exercises.filter((exercise) =>
      `${exercise.title} ${exercise.tags}`
        .toLowerCase()
        .includes(search.toLowerCase()),
    );
  }, [exercises, search]);

  async function submitExercise(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!form.title || !form.description || !form.tags || !form.midiaURL)
      return;

    try {
      const res = await fetch(`${API_URL}/api/exercise/create-exercise`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (res.ok) {
        const newExercise = await res.json();
        setExercises((prev) => [newExercise, ...prev]);
        setForm(emptyForm);
      } else {
        alert("Erro ao criar exercício.");
      }
    } catch (error) {
      console.error("Erro na requisição:", error);
    }
  }

  async function removeExercise(id: number) {
    try {
      const res = await fetch(
        `${API_URL}/api/exercise/deleteExerciseId/${id}`,
        {
          method: "DELETE",
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
  }

  return (
    <section className="grid grid-cols-4 items-start gap-4 md:grid-cols-12">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl">Cadastro de Exercícios</h1>
      </header>

      {/* Cadastrar exercício */}
      <div className="panel col-span-4 self-start md:col-span-8 p-5">
        <h2 className="text-xl">Adicionar um novo exercício</h2>
        <form
          onSubmit={submitExercise}
          className="grid grid-cols-4 md:grid-cols-12 gap-3 mt-3"
        >
          <input
            value={form.title}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, title: event.target.value }))
            }
            placeholder="Nome (Título) do exercício"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.tags}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, tags: event.target.value }))
            }
            placeholder="Tags (separadas por vírgula)"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <textarea
            value={form.description}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, description: event.target.value }))
            }
            placeholder="Descrição"
            className="col-span-4 min-h-34 rounded-md border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.midiaURL}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, midiaURL: event.target.value }))
            }
            placeholder="URL da Mídia / YouTube"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
            required
          />
          <button
            className="col-span-full rounded-md bg-blue mt-2 px-3 py-4 font-semibold text-neutral hover:opacity-70 transition duration-300 ease-in-out"
            type="submit"
          >
            Cadastrar exercício
          </button>
        </form>
      </div>

      {/* Busca no banco de exercícios */}
      <div className="col-span-4 p-5 md:col-span-4">
        <h2 className="text-xl">Buscar exercícios</h2>
        <input
          value={search}
          onChange={(event) => setSearch(event.target.value)}
          placeholder="Buscar por nome ou tag"
          className="mt-3 w-full rounded-md border border-slate-300 px-3 py-2 placeholder:text-neutral-700"
        />

        <div className="mt-4 space-y-4 h-[calc(100vh-14rem)] overflow-scroll no-scrollbar">
          {filtered.map((exercise) => (
            <article
              key={exercise.exercise_ID}
              className="relative rounded-md border border-slate-200 bg-[#FDFDFD] p-3 space-y-1"
            >
              <p className="font-semibold">{exercise.title}</p>
              <p className="mt-1 text-xs uppercase tracking-wide">
                {exercise.tags}
              </p>
              <a
                href={exercise.midiaURL}
                target="_blank"
                rel="noreferrer"
                className="mt-2 inline-block text-darker-blue underline underline-offset-2 hover:opacity-70 transition duration-300 ease-in-out"
              >
                Abrir video
              </a>
              <button
                onClick={() => removeExercise(exercise.exercise_ID)}
                className="absolute right-3 bottom-4 rounded-md bg-neutral-200 px-3 py-1 hover:opacity-70 transition duration-300 ease-in-out text-red-600"
              >
                Excluir
              </button>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
