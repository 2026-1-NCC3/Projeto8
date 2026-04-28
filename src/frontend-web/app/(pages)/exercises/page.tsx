/* eslint-disable @typescript-eslint/no-explicit-any */
'use client';

import { useMemo, useState } from 'react';
import {
  useExercises,
  type ExerciseRequest,
  type Exercise,
} from '@/app/hooks/useGetExercises';
import Image from 'next/image';

const emptyForm: ExerciseRequest = {
  title: '',
  description: '',
  tags: '',
  midiaURL: '',
};

export default function ExercisesPage() {
  const { exercises, addExercise, removeExercise, updateExercise } =
    useExercises();

  const [search, setSearch] = useState('');
  const [form, setForm] = useState<ExerciseRequest>(emptyForm);
  const [editingId, setEditingId] = useState<number | null>(null);

  const filtered = useMemo(() => {
    return exercises
      .filter((exercise) =>
        `${exercise.title} ${exercise.tags}`
          .toLowerCase()
          .includes(search.toLowerCase()),
      )
      .sort((a, b) => a.title.localeCompare(b.title));
  }, [exercises, search]);

  // Ativa o modo de edição e preenche o formulário
  const handleEditClick = (exercise: Exercise) => {
    setEditingId(exercise.exercise_id);
    setForm({
      title: exercise.title,
      description: exercise.description,
      tags: exercise.tags,
      midiaURL: exercise.midiaURL,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  // Limpa o formulário e sai do modo de edição
  const resetForm = () => {
    setEditingId(null);
    setForm(emptyForm);
  };

  async function submitExercise(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!form.title || !form.description || !form.tags || !form.midiaURL)
      return;

    const isSuccess = editingId
      ? await updateExercise(editingId, form)
      : await addExercise(form);

    if (isSuccess) {
      resetForm();
    }
  }

  return (
    <section className="grid grid-cols-4 items-start gap-x-4 gap-y-12 md:grid-cols-12 p-4">
      <header className="col-span-full">
        <h1 className="font-display text-4xl pt-6">Cadastro de Exercícios</h1>
      </header>

      {/* Área do Formulário */}
      <div className="panel col-span-4 self-start md:col-span-8">
        <h2 className="text-xl font-bold">
          {editingId
            ? `Editando: ${form.title}`
            : 'Adicionar um novo exercício'}
        </h2>
        <form
          onSubmit={submitExercise}
          className="grid grid-cols-4 md:grid-cols-12 gap-3 mt-3"
        >
          <input
            value={form.title}
            onChange={(e) => setForm((p) => ({ ...p, title: e.target.value }))}
            placeholder="Nome do exercício"
            className="col-span-4 rounded-md bg-white/30 border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.tags}
            onChange={(e) => setForm((p) => ({ ...p, tags: e.target.value }))}
            placeholder="Tags (separadas por vírgula)"
            className="col-span-4 rounded-md bg-white/30 border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <textarea
            value={form.description}
            onChange={(e) =>
              setForm((p) => ({ ...p, description: e.target.value }))
            }
            placeholder="Descrição"
            className="col-span-4 min-h-34 bg-white/30 rounded-md border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.midiaURL}
            onChange={(e) =>
              setForm((p) => ({ ...p, midiaURL: e.target.value }))
            }
            placeholder="URL do YouTube"
            className="col-span-4 rounded-md bg-white/30 border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
            required
          />

          <div className="col-span-full flex gap-2">
            <button
              className={`flex-1 rounded-md mt-2 px-3 py-4 text-neutral transition-all duration-300 ease-in-out ${
                editingId
                  ? 'bg-dark-blue hover:bg-blue'
                  : 'bg-dark-blue hover:bg-blue'
              }`}
              type="submit"
            >
              {editingId ? 'Salvar Alterações' : 'Cadastrar exercício'}
            </button>
            {editingId && (
              <button
                type="button"
                onClick={resetForm}
                className="mt-2 px-6 py-4 rounded-md bg-neutral-200 text-black/90 hover:bg-neutral-300 transition"
              >
                Cancelar
              </button>
            )}
          </div>
        </form>
      </div>

      {/* Área de Busca e Listagem */}
      <div className="col-span-4 md:col-span-4">
        <h2 className="text-xl font-bold">Buscar exercícios</h2>
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Buscar por nome ou tag"
          className="mt-3 w-full rounded-md border border-slate-300 px-3 py-2 placeholder:text-neutral-700 bg-white/30"
        />

        <div className="mt-4 space-y-4 h-[calc(100vh-17rem)] overflow-scroll no-scrollbar">
          {filtered.map((exercise: Exercise) => (
            <article
              key={exercise.exercise_id}
              className={`relative rounded-md border p-4 space-y-1 transition-all ${
                editingId === exercise.exercise_id
                  ? 'border-blue bg-blue/5 ring-1 ring-blue'
                  : 'border-slate-200 bg-white/30'
              }`}
            >
              <div className="flex justify-between items-start">
                <div>
                  <p className="font-semibold text-neutral-900">
                    {exercise.title}
                  </p>
                  <p className="text-xs uppercase tracking-wide text-neutral-500">
                    {exercise.tags}
                  </p>
                </div>

                {/* Botões de Ação com ícones */}
                <div className="flex gap-2">
                  <button
                    onClick={() => handleEditClick(exercise)}
                    className="p-1.5 rounded-md hover:bg-light-blue transition-colors bg-white/50 border border-slate-200"
                    title="Editar Exercício"
                  >
                    <Image
                      src="/edit-exercise.png"
                      alt="Editar"
                      width={18}
                      height={18}
                      className="object-contain"
                    />
                  </button>
                  <button
                    onClick={() => removeExercise(exercise.exercise_id)}
                    className="p-1.5 rounded-md hover:bg-red-500 group transition-colors bg-white/50 border border-slate-200"
                    title="Excluir Exercício"
                  >
                    <Image
                      src="/lixo.png"
                      alt="Excluir"
                      width={18}
                      height={18}
                      className="object-contain group-hover:brightness-200"
                    />
                  </button>
                </div>
              </div>

              <a
                href={exercise.midiaURL}
                target="_blank"
                rel="noreferrer"
                className="mt-3 inline-block text-darker-blue text-sm font-medium underline underline-offset-2 hover:opacity-70 transition"
              >
                Abrir vídeo
              </a>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
