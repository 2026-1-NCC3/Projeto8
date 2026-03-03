import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: {
    default: "Lumiere Admin | Projeto 8 - 3CCOMP",
    template: "%s | Lumiere Admin",
  },
  description:
    "Modulo web administrativo para gestao de pacientes, exercicios e acompanhamento clinico da fisioterapeuta Maya Yoshiko Yamamoto.",
  applicationName: "Lumiere Admin",
  authors: [{ name: "Grupo Lumiere - Projeto 8 - 3CCOMP" }],
  creator: "Grupo Lumiere",
  metadataBase: new URL("https://lumiere.local"),
  keywords: [
    "Lumiere",
    "Projeto 8",
    "3CCOMP",
    "Fisioterapia",
    "RPG",
    "Admin",
  ],
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR">
      <head>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="" />
        <link
          href="https://fonts.googleapis.com/css2?family=Archivo:ital,wght@0,100..900;1,100..900&family=Raleway:ital,wght@0,100..900;1,100..900&family=Stack+Sans+Headline:wght@200..700&display=swap"
          rel="stylesheet"
        />
      </head>
      <body className="antialiased">{children}</body>
    </html>
  );
}
