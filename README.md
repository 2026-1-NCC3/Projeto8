<h1 align="center">Fundação de Comércio Álvares Penteado</h1>

<div align="center">
<a href= "https://www.fecap.br/"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhZPrRa89Kma0ZZogxm0pi-tCn_TLKeHGVxywp-LXAFGR3B1DPouAJYHgKZGV0XTEf4AE&usqp=CAU" alt="FECAP - Fundação de Comércio Álvares Penteado" border="0"></a>

<h1>Nome do Projeto: Lumière</h1>
</div>
<br/>

## Integrantes do Grupo
[Analice Coimbra Carneiro](https://github.com/AnaliceCoimbra)  
[Mariah Alice Pimentel Lôbo Pereira](https://github.com/alicelobwp)  
[Sofia Botechia Hernandes](https://github.com/sofiahernandes)  
[Victória Duarte Vieira Azevedo](https://github.com/viick04)

## Professores Orientadores
[Katia Bossi](https://www.linkedin.com/in/katia-bossi)  
[Marco Aurélio](https://www.linkedin.com/in/marco-aurelio-lima-barbosa)  
[Victor Rosetti](https://www.linkedin.com/in/victorbarq)  
[Rodrigo da Rosa](https://www.linkedin.com/in/rodrigo-da-rosa-phd/)  
<br/>

## Entregas das Disciplinas
| Disciplina | Entrega 1 | Entrega 2 |  
| :-------: | :------: | :-------: |
| Análise Descritiva de Dados | [/documentos/entrega-1/analise-descitiva-de-dados](https://github.com/2026-1-NCC3/Projeto8/blob/main/documentos/entrega-1/analise-descitiva-de-dados.md) |    
| Programação Orientada a Objetos | [/documentos/entrega-1/programacao-orientada-objetos](https://github.com/2026-1-NCC3/Projeto8/blob/main/documentos/entrega-1/programacao-orientada-objetos.jpg) |   
| Programação para Dispositivos Móveis | [/src/frontend-app](https://github.com/2026-1-NCC3/Projeto8/tree/main/src/frontend-app) |   
| Projeto Interdisciplinar | [/src](https://github.com/2026-1-NCC3/Projeto8/tree/main/src) |   
<br/>

# Descrição
## Proposta Principal
O projeto **Lumière** é uma solução digital desenvolvida para auxiliar a fisioterapeuta **Maya Yoshiko Yamamoto**, especializada em **Reeducação Postural Global (RPG)**, na gestão e acompanhamento de seus pacientes.
Atualmente, parte da comunicação e do acompanhamento terapêutico é realizada por meio de mensagens e registros informais, o que dificulta a organização das informações clínicas, o acompanhamento da evolução do paciente e o planejamento dos exercícios domiciliares.

Para solucionar esse problema, o projeto propõe o desenvolvimento de um sistema composto por:
- **Aplicativo Mobile (paciente)**: onde o paciente poderá acessar seus exercícios prescritos, assistir vídeos demonstrativos, registrar a execução das atividades e acompanhar sua evolução ao longo do tratamento.
- **Módulo Web (admin)**: interface utilizada pela fisioterapeuta para gerenciar pacientes, prontuários, exercícios e acompanhar o progresso dos atendimentos.
- **Backend (comum entre os projetos Mobile e Web) e Banco de Dados**: responsável pela autenticação, regras de negócio, armazenamento das informações e integração entre o aplicativo mobile e o módulo web.
<br/>

## Design Gráfico
A identidade visual do projeto Lumière utiliza uma paleta de cores inspirada na antiga identidade visual da clínica; pensada a fim de transmitir **confiança, cuidado e clareza**, características importantes para aplicações na área da saúde.

### Paleta de Cores
| Cor | Hex Code |
|----|----|
| Light Blue | `#D1E8FF` |
| Blue | `#5979BE` |
| Dark Blue | `#0B1957` |
| Salmon | `#FF6142` |
| Light Salmon | `#FFBCAF` |
| Neutral | `#FEFDFC` |
| Black | `#020202` |
<br/>

## Trailer do Projeto
Em breve será disponibilizado um vídeo demonstrando:
- visão geral da plataforma
- funcionamento do aplicativo mobile
- uso do módulo web pelo profissional
- fluxo de prescrição e acompanhamento de exercícios

<br/>

## Estrutura de Pastas
O projeto possui uma arquitetura dividida em **frontend mobile, frontend web e backend**, todos dentro da pasta principal `src`.
```bash
src
├── backend
│ ├── controllers
│ ├── routes
│ ├── services
│ └── database
│
├── frontend-app
│ └── (Aplicativo mobile para pacientes)
│
└── frontend-web
├── components
├── app
├── services
├── styles
└── utils
```

### Descrição dos módulos
**/backend**  
Responsável por:
- autenticação;
- regras de negócio;
- comunicação com o banco de dados PostgreSQL;
- APIs consumidas pelo mobile e pelo web.

**/frontend-app**  
Aplicativo mobile utilizado pelos pacientes para:
- visualizar exercícios;
- registrar o progresso do paciente;
- acompanhar evolução e feedbacks;
- receber orientações do profissional.

**/frontend-web**  
Interface administrativa utilizada pela fisioterapeuta para:
- cadastrar e gerenciar pacientes;
- cadastrar exercícios;
- prescrever sessões de exercícios;
- acompanhar evolução e registros dos pacientes.
<br/>

### Tecnologias utilizadas no projeto:
- **Next.js (TypeScript)**
- **Tailwind CSS**
- **ShadCN UI**
- **Node.js**
<br/>

## Instalação Local
### Pré-requisitos
Para executar o projeto localmente é necessário ter instalado:
- **Node.js (versão 18 ou superior)**
- **npm ou yarn**
- **Git**
<br/>

### Passo-a-passo de execução local (Acesso ao App)
-
<br/>

### Passo-a-passo de execução local (Acesso ao Admin)
1. Clone o repositório
```bash
git clone https://github.com/2025-1-MCC1/Projeto8.git
```

2. Acesse a pasta do painel do administrador
```bash
cd Projeto8
cd frontend-web
```

3. Instale as dependências
```bash
npm install
```

4. Configure as variáveis de ambiente (exemplo `.env.example`)
```bash
NEXT_PUBLIC_API_URL=http://localhost:8080 # ou sua URL hospedada
```

5. Execute o servidor de desenvolvimento
```bash
npm run dev
```

6. Acesse no navegador
```bash
http://localhost:3000
```
<br/>

## Licença
[FECAP - Fundação de Comércio Álvares Penteado](https://www.fecap.br) - [Arkana](https://github.com/2025-1-MCC1/Projeto7) © 2025 by [Analice Coimbra Carneiro](https://github.com/analicecoimbra), [Mariah Alice Pimentel Lôbo Pereira](https://github.com/alicelobwp), [Sofia Botechia Hernandes](https://github.com/sofiahernandes) and [Victória Duarte Vieira Azevedo](https://github.com/viick04) is licensed under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/) <img src="https://mirrors.creativecommons.org/presskit/icons/cc.svg" height="20" width="20" style="margin-left: 0.2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/by.svg" height="20" width="20" style="margin-left: 0.2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/sa.svg" height="20" width="20" style="margin-left: 0.2em;">

<br/><br/>

## Referências
[Creative Commons](https://creativecommons.org/share-your-work/)  
[Template PI FECAP](https://github.com/fecaphub/Template_PI)  
