"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

type SidebarProps = {
  expanded: boolean;
  onToggle: () => void;
};

type NavItem = {
  href: string;
  label: string;
  icon: string;
};

const items: NavItem[] = [
  {
    href: "/exercises",
    label: "Exercícios",
    icon: '/exercises.png'
  },
  {
    href: "/patients/new",
    label: "Adicionar Paciente",
    icon: '/edit-user.png'
  },
  {
    href: "/patients",
    label: "Acompanhar Pacientes",
    icon: '/users.png'
  },
];

export function Sidebar({ expanded, onToggle }: SidebarProps) {
  const pathname = usePathname();
  const iconButtonSize = "h-11 w-11";

  return (
    <aside className="w-full p-3 transition-all duration-300">
      <div
        className={`flex items-start ${expanded ? "justify-between" : "justify-center"}`}
      >
        <div
          className={`overflow- transition-[max-width,opacity] duration-200 ${
            expanded
              ? "max-w-62 opacity-100"
              : "max-w-0 opacity-0"
            }`}
        >
          <p className="font-display text-2xl">Lumière</p>
        </div>

        <button
          onClick={onToggle}
          aria-expanded={expanded}
          aria-label={expanded ? "Recolher menu" : "Expandir menu"}
          className={`${iconButtonSize} grid place-items-center rounded-md bg-light-blue/60 text-blue transition-all duration-300 hover:bg-light-blue`}
          type="button"
        >
          <svg
            viewBox="0 0 24 24"
            className={`h-5 w-5 transition-transform duration-300 ${expanded ? "rotate-180" : "rotate-0"}`}
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <path d="m9 18 6-6-6-6" />
          </svg>
        </button>
      </div>

      <nav className="mt-6 flex flex-col gap-2">
        {items.map((item) => {
          const active = pathname === item.href;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center rounded-md  font-semibold transition-all duration-300 ${expanded
                  ? "h-11 w-full gap-3 px-4"
                  : `${iconButtonSize} mx-auto justify-center`
                } ${active
                  ? "bg-blue text-neutral"
                  : "bg-light-blue/45 text-black hover:bg-light-blue"
                }`}
              title={item.label}
            >
              <span className={`h-6 w-6 shrink-0 ${active && "invert"}`}><img src={item.icon}/></span>
              <span
                className={`overflow-hidden neutralspace-nowrap transition-[max-width,opacity,transform] duration-300 ${
                  expanded
                    ? "max-w-44 translate-x-0 opacity-100"
                    : "max-w-0 -translate-x-2 opacity-0"
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
