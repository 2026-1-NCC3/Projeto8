'use client';

import { useMemo, useState } from 'react';
import { useExercises, type ExerciseRequest, type Exercise } from '@/app/hooks/useGetExercises';
import Image from 'next/image';

const emptyForm: ExerciseRequest = {
  title: '',
  description: '',
  tags: '',
  midiaURL: '',
};

export default function ExercisesPage() {
  const { exercises, addExercise, removeExercise, updateExercise } = useExercises();

  const [search, setSearch] = useState('');
  const [form, setForm] = useState<ExerciseRequest>(emptyForm);
  const [editingId, setEditingId] = useState<number | null>(null);

  const filtered = useMemo(() => {
    return exercises.filter((exercise) =>
      `${exercise.title} ${exercise.tags}`
        .toLowerCase()
        .includes(search.toLowerCase()),
    );
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
    if (!form.title || !form.description || !form.tags || !form.midiaURL) return;

    const isSuccess = editingId
      ? await updateExercise(editingId, form)
      : await addExercise(form);

    if (isSuccess) {
      resetForm();
    }
  }

  return (
    <section className="grid grid-cols-4 items-start gap-4 md:grid-cols-12">
      <header className="col-span-full">
        <h1 className="font-display text-4xl pt-6">Cadastro de Exercícios</h1>
      </header>

      {/* Área do Formulário */}
      <div className="panel col-span-4 self-start md:col-span-8">
        <h2 className="text-xl">
          {editingId ? `Editando: ${form.title}` : 'Adicionar um novo exercício'}
        </h2>
        <form onSubmit={submitExercise} className="grid grid-cols-4 md:grid-cols-12 gap-3 mt-3">
          <input
            value={form.title}
            onChange={(e) => setForm(p => ({ ...p, title: e.target.value }))}
            placeholder="Nome do exercício"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.tags}
            onChange={(e) => setForm(p => ({ ...p, tags: e.target.value }))}
            placeholder="Tags (separadas por vírgula)"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-6 placeholder:text-neutral-700"
            required
          />
          <textarea
            value={form.description}
            onChange={(e) => setForm(p => ({ ...p, description: e.target.value }))}
            placeholder="Descrição"
            className="col-span-4 min-h-34 rounded-md border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
            required
          />
          <input
            value={form.midiaURL}
            onChange={(e) => setForm(p => ({ ...p, midiaURL: e.target.value }))}
            placeholder="URL do YouTube"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
            required
          />

          <div className="col-span-full flex gap-2">
            <button
              className={`flex-1 rounded-md mt-2 px-3 py-4 font-semibold text-neutral transition duration-300 ${editingId ? 'bg-dark-blue hover:bg-blue' : 'bg-blue hover:opacity-70'
                }`}
              type="submit"
            >
              {editingId ? 'Salvar Alterações' : 'Cadastrar exercício'}
            </button>
            {editingId && (
              <button
                type="button"
                onClick={resetForm}
                className="mt-2 px-6 py-4 rounded-md bg-neutral-200 text-neutral-700 font-semibold hover:bg-neutral-300 transition"
              >
                Cancelar
              </button>
            )}
          </div>
        </form>
      </div>

      {/* Área de Busca e Listagem */}
      <div className="col-span-4 md:col-span-4">
        <h2 className="text-xl">Buscar exercícios</h2>
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Buscar por nome ou tag"
          className="mt-3 w-full rounded-md border border-slate-300 px-3 py-2 placeholder:text-neutral-700"
        />

        <div className="mt-4 space-y-4 h-[calc(100vh-22rem)] overflow-scroll no-scrollbar">
          {filtered.map((exercise: Exercise) => (
            <article
              key={exercise.exercise_id}
              className={`relative rounded-md border p-3 space-y-1 transition-all ${editingId === exercise.exercise_id ? 'border-blue bg-blue/5 ring-1 ring-blue' : 'border-slate-200 bg-[#FDFDFD]'
                }`}
            >
              <p className="font-semibold">{exercise.title}</p>
              <p className="mt-1 text-xs uppercase tracking-wide">{exercise.tags}</p>
              <a
                href={exercise.midiaURL}
                target="_blank"
                rel="noreferrer"
                className="mt-2 inline-block text-darker-blue underline underline-offset-2 hover:opacity-70 transition"
              >
                Abrir video
              </a>

              <div className="absolute right-3 bottom-4 flex gap-2">
                <button
                  onClick={() => handleEditClick(exercise)}
                  className="rounded-md bg-neutral-100 text-blue px-3 py-1 hover:bg-blue hover:text-white transition duration-300"
                >
                  <Image
                    src="/edit-exercise.png"
                    alt="Editar Exercício"
                    className="w-5 h-5 object-contain"
                    width='20'
                    height='20'
                  />
                </button>
                <button
                  onClick={() => removeExercise(exercise.exercise_id)}
                  className="rounded-md bg-neutral-100 text-red-600 px-3 py-1 hover:bg-red-600 hover:text-white transition duration-300"
                >
                  <Image
                    src="/lixo.png"
                    alt="Deletar Exercício"
                    className="w-5 h-5 object-contain"
                    width='20'
                    height='20'
                  />
                </button>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}