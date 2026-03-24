"use client";

import { Sidebar } from "../components/sidebar";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  //Pafra ir para a pagina de login ao dar npm run dev, e logar 
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token && window.location.pathname !== "/login") {
      router.push("/login");
    } else {
      setIsAuthenticated(true);
    }
  }, []);

  if (!isAuthenticated) {
    return null;
  }

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
