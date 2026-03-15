"use client";

import { Sidebar } from "../components/sidebar";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="grid grid-cols-4 p-4 gap-4 md:block">
      <div className="col-span-4 md:flex md:items-start md:gap-4">
        <div className="fixed col-span-4">
          <Sidebar />
        </div>
        <main className="col-span-4 md:min-w-0 md:flex-1 mt-16">
          {children}
        </main>
      </div>
    </div>
  );
}
