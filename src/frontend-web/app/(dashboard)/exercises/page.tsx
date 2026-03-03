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
      `${exercise.name} ${exercise.tags}`.toLowerCase().includes(search.toLowerCase()),
    );
  }, [exercises, search]);

  function submitExercise(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!form.name || !form.description || !form.tags || !form.thumbnail || !form.youtube) return;

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
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="panel col-span-4 p-6 md:col-span-12">
        <h1 className="font-[family-name:var(--font-display)] text-[34px] text-[var(--dark-blue)]">Exercises</h1>
        <p className="mt-2 text-slate-600">Cadastro e manutencao do banco de exercicios (nome, descricao, tags, thumbnail e YouTube).</p>
      </header>

      <form onSubmit={submitExercise} className="panel col-span-4 grid grid-cols-4 gap-3 p-5 md:col-span-6 md:grid-cols-12">
        <input
          value={form.name}
          onChange={(event) => setForm((prev) => ({ ...prev, name: event.target.value }))}
          placeholder="Nome do exercicio"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-6"
          required
        />
        <input
          value={form.tags}
          onChange={(event) => setForm((prev) => ({ ...prev, tags: event.target.value }))}
          placeholder="Tags (separadas por virgula)"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-6"
          required
        />
        <textarea
          value={form.description}
          onChange={(event) => setForm((prev) => ({ ...prev, description: event.target.value }))}
          placeholder="Descricao"
          className="col-span-4 min-h-24 rounded-lg border border-slate-300 px-3 py-2 md:col-span-12"
          required
        />
        <input
          value={form.thumbnail}
          onChange={(event) => setForm((prev) => ({ ...prev, thumbnail: event.target.value }))}
          placeholder="URL da thumbnail"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-6"
          required
        />
        <input
          value={form.youtube}
          onChange={(event) => setForm((prev) => ({ ...prev, youtube: event.target.value }))}
          placeholder="Link do YouTube"
          className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-6"
          required
        />
        <button className="col-span-4 rounded-lg bg-[var(--salmon)] px-4 py-2 font-semibold text-white md:col-span-4" type="submit">
          Cadastrar exercicio
        </button>
      </form>

      <div className="panel col-span-4 p-5 md:col-span-6">
        <h2 className="font-semibold text-[22px] text-[var(--dark-blue)]">Busca rapida</h2>
        <input
          value={search}
          onChange={(event) => setSearch(event.target.value)}
          placeholder="Buscar por nome ou tag"
          className="mt-3 w-full rounded-lg border border-slate-300 px-3 py-2"
        />

        <div className="mt-4 space-y-3">
          {filtered.map((exercise) => (
            <article key={exercise.id} className="rounded-lg border border-slate-200 bg-slate-50 p-3">
              <p className="font-semibold">{exercise.name}</p>
              <p className="mt-1 text-sm text-slate-600">{exercise.description}</p>
              <p className="mt-1 text-xs uppercase tracking-wide text-[var(--dark-blue)]">{exercise.tags}</p>
              <a href={exercise.youtube} target="_blank" rel="noreferrer" className="mt-2 inline-block text-sm font-semibold text-[var(--salmon)]">
                Abrir video
              </a>
              <button onClick={() => removeExercise(exercise.id)} className="ml-3 rounded-lg bg-[var(--light-salmon)] px-3 py-1 text-sm">
                Excluir
              </button>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
