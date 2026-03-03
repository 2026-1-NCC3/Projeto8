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
  icon: (className?: string) => React.ReactNode;
};

const items: NavItem[] = [
  {
    href: "/exercises",
    label: "Exercises",
    icon: (className = "") => (
      <svg viewBox="0 0 24 24" className={className} fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M4 7h16M4 12h16M4 17h10" />
      </svg>
    ),
  },
  {
    href: "/patients/new",
    label: "Add Patient",
    icon: (className = "") => (
      <svg viewBox="0 0 24 24" className={className} fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M12 5v14M5 12h14" />
      </svg>
    ),
  },
  {
    href: "/patients",
    label: "Track Patient",
    icon: (className = "") => (
      <svg viewBox="0 0 24 24" className={className} fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M4 19V5m0 14h16M8 15l3-4 3 2 4-6" />
      </svg>
    ),
  },
];

export function Sidebar({ expanded, onToggle }: SidebarProps) {
  const pathname = usePathname();
  const iconButtonSize = "h-11 w-11";

  return (
    <aside className="panel w-full p-3 transition-all duration-300 md:min-h-[calc(100vh-2rem)] md:sticky md:top-4 md:self-start">
      <div className={`flex items-start ${expanded ? "justify-between" : "justify-center"}`}>
        <div
          className={`overflow-hidden transition-[max-width,opacity,transform] duration-300 ${
            expanded ? "max-w-52 translate-x-0 opacity-100" : "max-w-0 -translate-x-2 opacity-0"
          }`}
        >
          <p className="font-[family-name:var(--font-display)] text-2xl text-[var(--dark-blue)]">Lumiere</p>
        </div>

        <button
          onClick={onToggle}
          aria-expanded={expanded}
          aria-label={expanded ? "Recolher menu" : "Expandir menu"}
          className={`${iconButtonSize} grid place-items-center rounded-lg bg-[var(--light-blue)]/60 text-[var(--dark-blue)] transition-all duration-300 hover:bg-[var(--light-blue)]`}
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

      <nav className="mt-4 flex flex-col gap-2">
        {items.map((item) => {
          const active = pathname === item.href;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center rounded-lg text-sm font-semibold transition-all duration-300 ${
                expanded ? "h-11 w-full gap-3 px-4" : `${iconButtonSize} mx-auto justify-center`
              } ${
                active
                  ? "bg-[var(--dark-blue)] text-white"
                  : "bg-[var(--light-blue)]/45 text-[var(--black)] hover:bg-[var(--light-blue)]"
              }`}
              title={item.label}
            >
              <span className="h-5 w-5 shrink-0">{item.icon("h-5 w-5")}</span>
              <span
                className={`overflow-hidden whitespace-nowrap transition-[max-width,opacity,transform] duration-300 ${
                  expanded ? "max-w-40 translate-x-0 opacity-100" : "max-w-0 -translate-x-2 opacity-0"
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
