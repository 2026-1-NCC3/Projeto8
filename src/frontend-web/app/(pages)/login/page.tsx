"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault();
    // Fallback para produção caso o env não carregue
    const apiUrl = process.env.NEXT_PUBLIC_API_URL || "https://projeto8.onrender.com";

    try {
      const res = await fetch(`${apiUrl}/api/auth/login/admin`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          adminEmail: email,
          adminPassword: password,
        }),
      });

      if (!res.ok) {
        alert("Login inválido. Verifique suas credenciais.");
        return;
      }

      const data = await res.json();

      if (data.token) {
        localStorage.clear(); 
        
        localStorage.setItem("token", data.token);

        // Em vez de router.push, usar window.location garante que 
        // todos os estados do React sejam resetados com o novo token.
        window.location.href = "/exercises";
      } else {
        alert("Erro: O servidor não retornou um token válido.");
      }

    } catch (error) {
      console.error("Erro no login:", error);
      alert("Erro ao conectar com o servidor.");
    }
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '50px' }}>
      <h1>Login Administrativo</h1>

      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' }}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Senha"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit" style={{ padding: '10px', cursor: 'pointer' }}>
          Entrar
        </button>
      </form>
    </div>
  );
}