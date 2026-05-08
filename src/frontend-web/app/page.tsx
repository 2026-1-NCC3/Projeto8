'use client';

import Link from 'next/link';
import Image from 'next/image';

export default function HeroScreen() {
  return (
    <main className="relative w-screen h-screen overflow-hidden bg-neutral">
      <div className="absolute inset-0 z-0">
        <div className="absolute top-[-5%] left-[-5%] w-[70%] h-[60%] rounded-full bg-blue mix-blend-multiply filter blur-[120px] opacity-70 animate-pulse" />
        <div className="absolute bottom-0 right-[-4%] w-[60%] h-[70%] rounded-full bg-salmon mix-blend-multiply filter blur-[130px] opacity-50" />
      </div>

      <section className="relative z-10 flex flex-col justify-end h-full w-full px-10 py-20">
        <div className="grid grid-cols-1 md:grid-cols-2 items-end gap-16">
          
          <div className="flex flex-col items-start space-y-0 pb-10">
            <div>
               <Image
                src={'/maya-logo.png'}
                alt="Maya Logo Lumière"
                width={520}
                height={200}
                className="object-contain invert"
              />
            </div>
          </div>

          <div className="flex flex-col items-start md:max-w-md space-y-6">
            <div className="space-y-4">
              <h2 className="font-serif text-3xl md:text-4xl font-bold text-black">
                Plataforma de Fisioterapia
              </h2>
              <p className="font-sans text-sm md:text-base text-black/80 leading-relaxed text-justify">
                Como um sistema desenhado para excelência clínica, unimos tecnologia e cuidado humano. 
                Nossa expertise em gestão de exercícios e monitoramento de pacientes cria soluções de 
                alto impacto que equilibram estética visual com funcionalidade técnica.
              </p>
            </div>

            <Link
              href="/login"
              className="group relative inline-flex items-center justify-center overflow-hidden rounded-full px-8 py-3 bg-black text-white transition-all duration-300 hover:bg-black/80"
            >
              <span className="relative z-10 font-semibold">Acessar Sistema</span>
            </Link>
          </div>
        </div>

        <div className="absolute bottom-20 left-10 text-sm uppercase text-black">
          Lumière &copy; {new Date().getFullYear()} — Advanced Clinical Management
        </div>
      </section>
    </main>
  );
}