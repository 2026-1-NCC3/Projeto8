"use client";

import { signIn } from "next-auth/react";
import { toast } from "sonner";

const Heading = () => {
  const onSignIn = async () => {
    try {
      const response = await signIn("google", {
        callbackUrl: "/patients",
        redirect: false,
      });

      if (response?.error) {
        toast.error("Sign-in failed. Check server logs for details.");
        return;
      }

      if (response?.url) {
        window.location.href = response.url;
      }
    } catch (error) {
      console.error(error);
      toast.error("Sign-in failed. Check server logs for details.");
    }
  };

  return (
    <div className="max-w-3xl space-y-4">
      <h1 className="text-2xl sm:text-3xl md:text-4xl font-bold">
        Bem-vinda, Maya! Para acessar o painel, faça a autenticação com sua conta Google
      </h1>
      <button className="my-6" onClick={onSignIn}>
        Sign-in com o Google
      </button>
    </div>
  );
};

export { Heading };
