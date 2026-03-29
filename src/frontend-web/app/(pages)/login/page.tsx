"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault();
    const apiUrl = process.env.NEXT_PUBLIC_API_URL

    try {
      const res = await fetch(`${apiUrl}/api/auth/login/admin`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          adminName: "",
          adminEmail: email,
          adminPassword: password,
        }),
      });

      if (!res.ok) {
        alert("Login inválido");
        return;
      }

      const data = await res.json();

      // salva o token
      localStorage.setItem("token", data.token);

      // redireciona
      router.push("/exercises");
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <div>
      <h1>Login</h1>

      <form onSubmit={handleLogin}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Senha"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="submit">Entrar</button>
      </form>
    </div>
  );
}