"use client";

import { useMemo, useState } from "react";
import { initialExercises, type Exercise } from "../../lib/mock-data";

type ExerciseForm = {
  name: string;
  description: string;
  tags: string;
  thumbnail: string;
  youtube: string;
};

const emptyForm: ExerciseForm = {
  name: "",
  description: "",
  tags: "",
  thumbnail: "",
  youtube: "",
};

export default function ExercisesPage() {
  const [exercises, setExercises] = useState<Exercise[]>(initialExercises);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState<ExerciseForm>(emptyForm);

  const filtered = useMemo(() => {
    return exercises.filter((exercise) =>
      `${exercise.name} ${exercise.tags}`
        .toLowerCase()
        .includes(search.toLowerCase()),
    );
  }, [exercises, search]);

  function submitExercise(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (
      !form.name ||
      !form.description ||
      !form.tags ||
      !form.thumbnail ||
      !form.youtube
    )
      return;

    const next: Exercise = {
      id: `EX-${Date.now()}`,
      name: form.name,
      description: form.description,
      tags: form.tags,
      thumbnail: form.thumbnail,
      youtube: form.youtube,
    };

    setExercises((prev) => [next, ...prev]);
    setForm(emptyForm);
  }

  function removeExercise(id: string) {
    setExercises((prev) => prev.filter((exercise) => exercise.id !== id));
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
            value={form.name}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, name: event.target.value }))
            }
            placeholder="Nome do exercício"
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
            value={form.thumbnail}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, thumbnail: event.target.value }))
            }
            placeholder="URL da thumbnail (capa)"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.youtube}
            onChange={(event) =>
              setForm((prev) => ({ ...prev, youtube: event.target.value }))
            }
            placeholder="Link do YouTube"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
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
              key={exercise.id}
              className="relative rounded-md border border-slate-200 bg-[#FDFDFD] p-3 space-y-1"
            >
              <p className="font-semibold">{exercise.name}</p>
              <p className="mt-1 text-xs uppercase tracking-wide">
                {exercise.tags}
              </p>
              <a
                href={exercise.youtube}
                target="_blank"
                rel="noreferrer"
                className="mt-2 inline-block text-darker-blue underline underline-offset-2 hover:opacity-70 transition duration-300 ease-in-out"
              >
                Abrir video
              </a>
              <button
                onClick={() => removeExercise(exercise.id)}
                className="absolute right-3 bottom-4 rounded-md bg-neutral-200 px-3 py-1 hover:opacity-70 transition duration-300 ease-in-out"
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
