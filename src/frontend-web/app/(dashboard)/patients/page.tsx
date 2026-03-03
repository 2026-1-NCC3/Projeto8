"use client";

import { useMemo, useState } from "react";
import { initialPatients, initialSchedule, type Patient } from "../../lib/mock-data";

export default function PatientsPage() {
  const [patients, setPatients] = useState<Patient[]>(initialPatients);
  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState(initialPatients[0]?.id ?? "");
  const [schedules, setSchedules] = useState(initialSchedule);
  const [scheduleForm, setScheduleForm] = useState({
    exerciseName: "",
    frequency: "",
    orientation: "",
  });

  const filteredPatients = useMemo(() => {
    return patients.filter((patient) => {
      const fullName = `${patient.firstName} ${patient.lastName}`.toLowerCase();
      return fullName.includes(query.toLowerCase());
    });
  }, [patients, query]);

  const selectedPatient = patients.find((patient) => patient.id === selectedId) ?? filteredPatients[0];
  const patientSchedules = schedules.filter((item) => item.patientId === selectedPatient?.id);

  function deletePatient(id: string) {
    setPatients((prev) => prev.filter((patient) => patient.id !== id));
    setSchedules((prev) => prev.filter((item) => item.patientId !== id));
    if (selectedId === id) {
      const next = patients.find((patient) => patient.id !== id);
      setSelectedId(next?.id ?? "");
    }
  }

  function submitSchedule(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedPatient) return;
    if (!scheduleForm.exerciseName || !scheduleForm.frequency || !scheduleForm.orientation) return;

    setSchedules((prev) => [
      {
        id: `SCH-${Date.now()}`,
        patientId: selectedPatient.id,
        exerciseName: scheduleForm.exerciseName,
        frequency: scheduleForm.frequency,
        orientation: scheduleForm.orientation,
      },
      ...prev,
    ]);

    setScheduleForm({ exerciseName: "", frequency: "", orientation: "" });
  }

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="panel col-span-4 p-6 md:col-span-12">
        <h1 className="font-[family-name:var(--font-display)] text-[34px] text-[var(--dark-blue)]">Track Patient</h1>
        <p className="mt-2 text-slate-600">Historico clinico, acompanhamento e ajustes de conduta.</p>
      </header>

      <div className="panel col-span-4 p-5 md:col-span-7">
        <div className="grid grid-cols-4 gap-3 md:grid-cols-12">
          <input
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Buscar por nome"
            className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-12"
          />
        </div>

        <div className="mt-4 overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead>
              <tr className="border-b border-slate-200 text-slate-600">
                <th className="py-2">Paciente</th>
                <th className="py-2">Dor</th>
                <th className="py-2">Acoes</th>
              </tr>
            </thead>
            <tbody>
              {filteredPatients.map((patient) => (
                <tr key={patient.id} className="border-b border-slate-100">
                  <td className="py-3">
                    <button
                      onClick={() => setSelectedId(patient.id)}
                      className="font-semibold text-[var(--dark-blue)]"
                    >
                      {patient.firstName} {patient.lastName}
                    </button>
                  </td>
                  <td className="py-3">{patient.painLevel}/10</td>
                  <td className="py-3">
                    <div className="flex gap-2">
                      <button
                        onClick={() => deletePatient(patient.id)}
                        className="rounded-lg bg-[var(--light-salmon)] px-3 py-1"
                      >
                        Excluir
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="panel col-span-4 p-5 md:col-span-5">
        <h2 className="font-semibold text-[22px] text-[var(--dark-blue)]">Prontuario e Evolucao</h2>
        {selectedPatient ? (
          <div className="mt-3 space-y-3 text-sm">
            <p><span className="font-semibold">Paciente:</span> {selectedPatient.firstName} {selectedPatient.lastName}</p>
            <p><span className="font-semibold">Diagnostico:</span> {selectedPatient.diagnosis}</p>
            <p><span className="font-semibold">Ultima sessao:</span> {selectedPatient.lastSession}</p>
            <p><span className="font-semibold">Escala de dor:</span> {selectedPatient.painLevel}/10</p>
            <div>
              <p className="mb-1 font-semibold">Aderencia ao plano</p>
              <div className="h-3 rounded-full bg-slate-200">
                <div
                  className="h-3 rounded-full bg-[var(--salmon)]"
                  style={{ width: `${selectedPatient.adherence}%` }}
                />
              </div>
              <p className="mt-1 text-xs text-slate-600">{selectedPatient.adherence}% de execucao registrada</p>
            </div>
            <button className="rounded-lg bg-[var(--dark-blue)] px-4 py-2 text-white">Acessar prontuario completo</button>
          </div>
        ) : (
          <p className="mt-3 text-sm text-slate-500">Selecione um paciente para visualizar o prontuario.</p>
        )}
      </div>

      <div className="panel col-span-4 p-5 md:col-span-12">
        <h2 className="font-semibold text-[22px] text-[var(--dark-blue)]">Calendario Personalizado do Paciente</h2>
        <form onSubmit={submitSchedule} className="mt-3 grid grid-cols-4 gap-3 md:grid-cols-12">
          <input
            value={scheduleForm.exerciseName}
            onChange={(event) => setScheduleForm((prev) => ({ ...prev, exerciseName: event.target.value }))}
            placeholder="Exercicio"
            className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-3"
          />
          <input
            value={scheduleForm.frequency}
            onChange={(event) => setScheduleForm((prev) => ({ ...prev, frequency: event.target.value }))}
            placeholder="Frequencia"
            className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-3"
          />
          <input
            value={scheduleForm.orientation}
            onChange={(event) => setScheduleForm((prev) => ({ ...prev, orientation: event.target.value }))}
            placeholder="Orientacoes"
            className="col-span-4 rounded-lg border border-slate-300 px-3 py-2 md:col-span-4"
          />
          <button className="col-span-4 rounded-lg bg-[var(--salmon)] px-4 py-2 font-semibold text-white md:col-span-2" type="submit">
            Associar
          </button>
        </form>

        <div className="mt-4 grid grid-cols-4 gap-3 md:grid-cols-12">
          {patientSchedules.length > 0 ? (
            patientSchedules.map((item) => (
              <article key={item.id} className="col-span-4 rounded-lg border border-slate-200 bg-slate-50 p-3 md:col-span-4">
                <p className="font-semibold">{item.exerciseName}</p>
                <p className="text-sm text-slate-600">{item.frequency}</p>
                <p className="mt-1 text-sm">{item.orientation}</p>
              </article>
            ))
          ) : (
            <p className="col-span-4 text-sm text-slate-500 md:col-span-12">Nenhum exercicio associado para o paciente selecionado.</p>
          )}
        </div>
      </div>
    </section>
  );
}
