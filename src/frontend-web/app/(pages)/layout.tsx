export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="md:block min-h-screen">
      <main className="col-span-4 md:min-w-0 md:flex-1">{children}</main>
    </div>
  );
}
