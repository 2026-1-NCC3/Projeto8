"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault();
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

      if (res.ok) {
        router.push("/dashboard");
      } else {
        alert("Login failed. Please check your credentials.");
      }
    } catch (error) {
      console.error("Error during login:", error);
      alert("An error occurred. Please try again later.");
    }
  }

  return (
    <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh", backgroundColor: "#f3f4f6" }}>
      <form
        onSubmit={handleLogin}
        style={{
          backgroundColor: "#ffffff",
          padding: "2rem",
          borderRadius: "8px",
          boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
          width: "100%",
          maxWidth: "400px",
        }}
      >
        <h2 style={{ textAlign: "center", marginBottom: "1.5rem", color: "#333" }}>Admin Login</h2>
        <div style={{ marginBottom: "1rem" }}>
          <label htmlFor="email" style={{ display: "block", marginBottom: "0.5rem", color: "#555" }}>
            Email
          </label>
          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={{
              width: "100%",
              padding: "0.75rem",
              border: "1px solid #ddd",
              borderRadius: "4px",
              fontSize: "1rem",
            }}
            placeholder="Enter your email"
          />
        </div>
        <div style={{ marginBottom: "1.5rem" }}>
          <label htmlFor="password" style={{ display: "block", marginBottom: "0.5rem", color: "#555" }}>
            Password
          </label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{
              width: "100%",
              padding: "0.75rem",
              border: "1px solid #ddd",
              borderRadius: "4px",
              fontSize: "1rem",
            }}
            placeholder="Enter your password"
          />
        </div>
        <button
          type="submit"
          style={{
            width: "100%",
            padding: "0.75rem",
            backgroundColor: "#4f46e5",
            color: "#ffffff",
            border: "none",
            borderRadius: "4px",
            fontSize: "1rem",
            cursor: "pointer",
          }}
        >
          Login
        </button>
      </form>
    </div>
  );
}
