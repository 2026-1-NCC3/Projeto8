'use client';

import Link from 'next/link';
import MayaLogo from '@/public/maya-logo.svg';
import Image from 'next/image';

export default function HeroScreen() {
  return (
    <section className="flex h-screen w-full bg-dark-blue flex-col items-center justify-center p-6">
      {/* Conteúdo de Texto e Botão */}
      <div className="mb-12 flex flex-col items-center space-y-6 text-center animate-in slide-in-from-bottom duration-1000">
        <div className="flex w-screen flex-1 items-center justify-center">
          <Image
            src={'/maya-logo.png'}
            alt="Maya Logo Lumière"
            width={656}
            height={176}
          />
        </div>
        <p className="mx-auto max-w-md text-lg text-white">
          Gestão inteligente para fisioterapia: treinos, consultas e evolução
          clínica em um só lugar.
        </p>

        <Link
          href="/login"
          className="group relative flex items-center justify-center overflow-hidden rounded-full bg-dark-blue px-10 py-4 text-xl font-bold text-white transition-all hover:bg-blue hover:shadow-[0_0_20px_rgba(0,112,243,0.4)] active:scale-95"
        >
          <span className="relative z-10">Acessar Sistema</span>
        </Link>
      </div>

      {/* Detalhe decorativo no rodapé */}
      <div className="absolute bottom-4 text-xs font-medium uppercase tracking-widest text-neutral-400">
        Lumiere &copy; {new Date().getFullYear()}
      </div>
    </section>
  );
}
