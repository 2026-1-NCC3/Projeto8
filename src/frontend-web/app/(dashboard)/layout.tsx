"use client";

import { useState } from "react";
import { Sidebar } from "../components/sidebar";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const [expanded, setExpanded] = useState(false);

  return (
    <div className="app-shell p-4">
      <div className="grid grid-cols-4 gap-4 md:block">
        <div className="col-span-4 md:flex md:items-start md:gap-4">
          <div
            className="col-span-4 md:shrink-0 md:transition-[width] md:duration-300 md:w-[var(--sidebar-width)]"
            style={{ ["--sidebar-width" as string]: expanded ? "17rem" : "4.25rem" }}
          >
            <Sidebar expanded={expanded} onToggle={() => setExpanded((prev) => !prev)} />
          </div>
          <main className="col-span-4 md:min-w-0 md:flex-1">{children}</main>
        </div>
      </div>
    </div>
  );
}
