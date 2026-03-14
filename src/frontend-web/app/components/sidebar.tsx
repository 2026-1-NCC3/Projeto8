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
    <aside className="flex gap-4 items-center w-full p-2 transition-all duration-300 bg-neutral rounded-2xl border border-dark-blue/20">
      <nav className="flex gap-2">
        {items.map((item) => {
          const active = pathname === item.href;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center rounded-md font-semibold transition-all duration-300 ${
                active
                  ? "h-11 w-full gap-3 px-4"
                  : 'w-12 px-6 mx-auto justify-center!'
              } ${
                active
                  ? "bg-dark-blue text-neutral"
                  : "bg-light-blue/45 text-black hover:bg-light-blue"
              }`}
            >
              <span className={`h-6 w-6 shrink-0 ${active && "invert"}`}>
                <img src={item.icon} />
              </span>
              <span
                className={`neutralspace-nowrap transition-[opacity,transform] duration-100 ${
                  active
                    ? "w-fit opacity-100"
                    : "w-0 opacity-0"
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
