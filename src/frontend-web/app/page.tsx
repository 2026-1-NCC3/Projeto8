'use client';

import Link from 'next/link';
import Image from 'next/image';
import Gradient from './components/gradient';

export default function HeroScreen() {
  return (
    <Gradient className={ "bg-black w-screen! h-screen overflow-clip flex justify-center content-center" }>
    <section className="flex h-screen w-full flex-col items-center justify-center p-6">
      {/* Conteúdo de Texto e Botão */}
      <div className="flex flex-col items-center space-y-6 text-center animate-in slide-in-from-bottom duration-1000">
        <div className="flex w-screen flex-1 items-center justify-center">
          <Image
            src={'/maya-logo.png'}
            alt="Maya Logo Lumière"
            width={656}
            height={176}
          />
        </div>

        <Link
          href="/login"
          className="group relative flex items-center justify-center overflow-hidden rounded-md px-6 py-2 border border-white text-white transition-all duration-300 hover:bg-white hover:text-dark-blue"
        >
          <span className="relative z-10">Acessar Sistema</span>
        </Link>
      </div>

      {/* Detalhe decorativo no rodapé */}
      <div className="absolute bottom-4 text-sm font-medium uppercase text-white">
        Lumiere &copy; {new Date().getFullYear()}
      </div>
    </section>
    </Gradient>
  );
}
