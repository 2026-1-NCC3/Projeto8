/* eslint-disable @next/next/no-img-element */
"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

type NavItem = {
  href: string;
  label: string;
  icon: string;
};

const items: NavItem[] = [
  {
    href: "/exercises",
    label: "Exercícios",
    icon: "/exercises.png",
  },
  {
    href: "/patients/new",
    label: "Adicionar Paciente",
    icon: "/edit-user.png",
  },
  {
    href: "/patients",
    label: "Acompanhar Pacientes",
    icon: "/users.png",
  },
];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="flex items-center w-full p-2 bg-neutral rounded-md border border-dark-blue/20">
      <nav className="flex gap-2">
        {items.map((item) => {
          const active = pathname === item.href;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center h-11 rounded-md font-semibold overflow-hidden transition-all duration-400 ease-in-out ${
                active
                  ? "gap-3 px-4 bg-dark-blue text-neutral"
                  : "w-11 justify-center px-0 bg-light-blue/45 text-black hover:bg-light-blue"
              }`}
            >
              <span
                className={`flex items-center justify-center h-6 w-6 shrink-0 transition-transform duration-400 ${
                  active && "invert"
                }`}
              >
                <img
                  src={item.icon}
                  alt={item.label}
                  className="w-full h-full object-contain"
                />
              </span>
              <span
                className={`whitespace-nowrap overflow-hidden transition-all duration-400 ease-in-out ${
                  active ? "max-w-50 opacity-100" : "max-w-0 opacity-0"
                }`}
              >
                {item.label}
              </span>
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
