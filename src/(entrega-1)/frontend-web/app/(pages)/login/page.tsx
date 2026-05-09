'use client';

import { useActionState } from 'react';
import { authenticateAdmin } from '@/app/actions/auth';

const initialState = {
  error: '',
};

export default function LoginPage() {
  const [state, formAction, isPending] = useActionState(
    authenticateAdmin,
    initialState,
  );

  return (
    <div className="flex min-h-screen items-center justify-center p-4">
      <form
        action={formAction}
        className="w-full max-w-100 rounded-md border border-gray-200 bg-white px-8 py-10"
      >
        <h2 className="mb-8 text-center font-sans text-2xl font-semibold text-gray-900">
          Admin Login
        </h2>

        {state?.error && (
          <div className="mb-4 rounded-md bg-salmon/50 p-3 text-sm text-salmon">
            {state.error}
          </div>
        )}

        <div className="mb-5">
          <label
            htmlFor="email"
            className="mb-2 block font-sans text-sm font-medium text-gray-700"
          >
            Email
          </label>
          <input
            id="email"
            name="email"
            type="email"
            className="w-full rounded-md border border-gray-300 p-3 text-base outline-none transition-all focus:border-dark-blue focus:ring-1 focus:ring-dark-blue placeholder:text-gray-600!"
            placeholder="admin@lumiere.com"
            required
          />
        </div>

        <div className="mb-8">
          <label
            htmlFor="password"
            className="mb-2 block font-sans text-sm font-medium text-gray-700"
          >
            Password
          </label>
          <input
            id="password"
            name="password"
            type="password"
            className="w-full rounded-md border border-gray-300 p-3 text-base outline-none transition-all focus:border-dark-blue focus:ring-1 focus:ring-dark-blue placeholder:text-gray-600!"
            placeholder="••••••••"
            required
          />
        </div>

        <button
          type="submit"
          disabled={isPending}
          className="w-full rounded-md bg-black p-3.5 text-base font-semibold text-white transition-colors duration-200 hover:bg-black/80 disabled:cursor-not-allowed disabled:bg-black/30"
        >
          {isPending ? 'Signing in...' : 'Log in'}
        </button>
      </form>
    </div>
  );
}
