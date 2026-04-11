"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function authenticateAdmin(_: unknown, formData: FormData) {
  const email = formData.get("email");
  const password = formData.get("password");

  const apiUrl = process.env.NEXT_PUBLIC_API_URL;

  try {
    const res = await fetch(`${apiUrl}/api/auth/login/admin`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        adminEmail: email,
        adminPassword: password,
      }),
    });

    if (!res.ok) {
      return { error: "Invalid credentials" };
    }

    const data = await res.json();

    const cookieStore = await cookies();

    cookieStore.set({
      name: "lumiere_admin_token",
      value: data.token,
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      path: "/",
      maxAge: 60 * 60 * 24 * 7,
    });
  } catch (error) {
    return { error: "Falha ao conectar com o servidor." };
  }

  redirect("/exercises");
}
