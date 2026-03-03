export type Patient = {
  id: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  cpf: string;
  diagnosis: string;
  painLevel: number;
  adherence: number;
  lastSession: string;
};

export type Exercise = {
  id: string;
  name: string;
  description: string;
  tags: string;
  thumbnail: string;
  youtube: string;
};

export type Schedule = {
  id: string;
  patientId: string;
  exerciseName: string;
  frequency: string;
  orientation: string;
};

export const initialPatients: Patient[] = [
  {
    id: "PAT-001",
    firstName: "Ana",
    lastName: "Silva",
    birthDate: "1995-04-13",
    cpf: "111.222.333-44",
    diagnosis: "Hipercifose toracica",
    painLevel: 4,
    adherence: 78,
    lastSession: "2026-03-01",
  },
  {
    id: "PAT-002",
    firstName: "Bruno",
    lastName: "Costa",
    birthDate: "1989-10-28",
    cpf: "222.333.444-55",
    diagnosis: "Escoliose lombar",
    painLevel: 6,
    adherence: 52,
    lastSession: "2026-02-20",
  },
  {
    id: "PAT-003",
    firstName: "Carla",
    lastName: "Melo",
    birthDate: "2001-07-06",
    cpf: "333.444.555-66",
    diagnosis: "Retificacao cervical",
    painLevel: 3,
    adherence: 89,
    lastSession: "2026-03-02",
  },
];

export const initialExercises: Exercise[] = [
  {
    id: "EX-001",
    name: "Alongamento peitoral na parede",
    description: "Mobiliza cintura escapular e melhora alinhamento de ombros.",
    tags: "postura,toracica,ombros",
    thumbnail: "https://images.unsplash.com/photo-1518611012118-696072aa579a",
    youtube: "https://www.youtube.com/watch?v=4BOTvaRaDjI",
  },
  {
    id: "EX-002",
    name: "Respiracao costodiafragmatica",
    description: "Treino respiratorio para expansao toracica e controle de tensao.",
    tags: "respiracao,rpg",
    thumbnail: "https://images.unsplash.com/photo-1506126613408-eca07ce68773",
    youtube: "https://www.youtube.com/watch?v=kgTL5G1ibIo",
  },
];

export const initialSchedule: Schedule[] = [
  {
    id: "SCH-001",
    patientId: "PAT-001",
    exerciseName: "Alongamento peitoral na parede",
    frequency: "3x por semana",
    orientation: "2 series de 40 segundos, sem dor irradiada.",
  },
  {
    id: "SCH-002",
    patientId: "PAT-003",
    exerciseName: "Respiracao costodiafragmatica",
    frequency: "Diario",
    orientation: "8 minutos, foco em expiracao lenta.",
  },
];
