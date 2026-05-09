import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  // Puxa o cookie que salvamos na Server Action
  const token = request.cookies.get('lumiere_admin_token')?.value;

  // Se o usuário não tem token e está tentando acessar uma página que não seja o /login
  if (!token && !request.nextUrl.pathname.startsWith('/login')) {
    // Redireciona de volta pro login automaticamente
    return NextResponse.redirect(new URL('/login', request.url));
  }

  // Se já está logado e tenta acessar a tela de /login de novo
  if (token && request.nextUrl.pathname.startsWith('/login')) {
    // Redireciona pro painel automaticamente
    return NextResponse.redirect(new URL('/exercises', request.url));
  }

  return NextResponse.next();
}

// Configura em quais rotas o middleware deve rodar
export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
};
