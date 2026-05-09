'use client';

import { usePathname } from 'next/navigation';
import { Sidebar } from '../../components/sidebar';
import { motion, AnimatePresence } from 'framer-motion';
import { useEffect, useState } from 'react';

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setIsMounted(true);
    }, 800);
  }, []);

  return (
    <div className="grid grid-cols-4 pt-4 px-4 gap-4 md:block">
      <div className="col-span-4 md:flex md:items-start md:gap-4 h-full">
        <div className="absolute col-span-4">
          <Sidebar />
        </div>
        <main className="col-span-4 md:min-w-0 md:flex-1 mt-16">
          {isMounted && (
          <AnimatePresence mode="wait">
            <motion.div
              key={pathname}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ 
                duration: 1,
                ease: "easeInOut",
              }}
            >
              {children}
            </motion.div>
          </AnimatePresence>
        )}
        </main>
      </div>
    </div>
  );
}
