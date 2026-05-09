/* eslint-disable @next/next/no-img-element */
'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

type NavItem = {
  href: string;
  label: string;
  icon: string;
};

const items: NavItem[] = [
  {
    href: '/exercises',
    label: 'Exercícios',
    icon: '/exercises.png',
  },
  {
    href: '/patients',
    label: 'Pacientes',
    icon: '/users.png',
  },
  {
    href: '/add-patient',
    label: 'Adicionar Paciente',
    icon: '/edit-user.png',
  },
  {
    href: '/calendars',
    label: 'Gerencias Calendários',
    icon: '/calendar.png',
  },
];

export function Sidebar() {
  const pathname = usePathname();

  const isActive = (href: string) => {
    if (pathname === href) return true;

    // Keep "Pacientes" active for nested routes like /patients/new.
    if (href === '/patients') {
      return pathname.startsWith('/patients/');
    }

    return false;
  };

  return (
    <aside className="flex items-center w-full p-2 bg-neutral rounded-md border border-dark-blue/20">
      <nav className="flex gap-2">
        {items.map((item) => {
          const active = isActive(item.href);
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center h-11 rounded-md font-semibold overflow-hidden transition-all duration-300 ease-in-out ${
                active
                  ? 'gap-3 px-4 bg-dark-blue text-neutral'
                  : 'w-11 justify-center px-0 bg-blue/45 hover:bg-blue'
              }`}
            >
              <span
                className={`flex items-center justify-center h-6 w-6 shrink-0 ${
                  active && 'invert brightness-0'
                }`}
              >
                <img
                  src={item.icon}
                  alt={item.label}
                  className="w-full h-full object-contain"
                />
              </span>
              <span
                className={`text-white! whitespace-nowrap overflow-hidden transition-all duration-300 ease-in-out ${
                  active ? 'max-w-50 opacity-100' : 'max-w-0 opacity-0'
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
