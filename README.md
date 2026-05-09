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
| Projeto Interdisciplinar | [/documentos/entrega-1/projeto-interdisciplinar](https://github.com/2026-1-NCC3/Projeto8/tree/main/documentos/entrega-1/projeto-interdisciplinar) |   
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

## 🛠 Estrutura de Pastas
O projeto possui uma arquitetura dividida em **frontend mobile, frontend web e backend**, todos dentro da pasta principal `src`.
```bash
src
├── backend
│ src/main/java/com/example/MayaFisioLumiere
│ ├── Configuration
│ ├── Controller
│ ├── Domain
│ ├── Entity
│ ├── Repository
│ └── Services
│
├── frontend-app
│ src/main/java/com/example/projeto8
│ ├── adapter
│ ├── api
│ ├── model
│ ├── remote
│ ├── UI
│ res
│
├── frontend-web
│ ├── (pages)
│ ├── actions
│ ├── components
│ ├── hooks
└─├── lib
```

**Descrição dos módulos**  
**/backend**  
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

## 🛠 Instalação
- **Android**: faça o Download do app-debug.apk no seu celular. Execute o APK e siga as instruções de seu telefone.
- **Web App**: não há instalação! Acesse a [aplicação web](https://admin-lumiere.vercel.app/) através do seu browser de preferência e insira suas credenciais.

<br/>

## 💻 Configuração para Desenvolvimento
### Pré-requisitos
Para acessar o ambiente de desenvolvimento é necessário ter instalado:
- **Android Studio** e **Visual Studio Code** (ou outra aplicação equivalente)
- **Java (JDK versão 21)**
- **Node.js (versão 18 ou superior)** e **npm ou yarn**
- **Git**
<br/>

### Passo-a-passo de execução local (Acesso ao App)
Para acessar o ambiente de desenvolvimento do aplicativo mobile:
1. Clone o repositório
```bash
git clone https://github.com/2026-1-NCC3/Projeto8.git
```

2. Acesse a pasta do applicativo
```bash
cd Projeto8
cd frontend-app
```

6. Abra a pasta no Android Studio  

<br/>

### Passo-a-passo de execução local (Acesso ao Admin)
1. Clone o repositório
```bash
git clone https://github.com/2026-1-NCC3/Projeto8.git
```

2. Acesse a pasta do painel do administrador e instale as dependências  
```bash
cd Projeto8
cd frontend-web
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

## 📋 Licença/License
[FECAP - Fundação de Comércio Álvares Penteado](https://www.fecap.br) - [Lumière](https://github.com/2026-1-NCC3/Projeto8) © 2026 by [Analice Coimbra Carneiro](https://github.com/analicecoimbra), [Mariah Alice Pimentel Lôbo Pereira](https://github.com/alicelobwp), [Sofia Botechia Hernandes](https://github.com/sofiahernandes) and [Victória Duarte Vieira Azevedo](https://github.com/viick04) is licensed under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/) <img src="https://mirrors.creativecommons.org/presskit/icons/cc.svg" height="20" width="20" style="margin-left: 0.2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/by.svg" height="20" width="20" style="margin-left: 0.2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/sa.svg" height="20" width="20" style="margin-left: 0.2em;">

<br/><br/>

## 🎓 Referências
[Creative Commons](https://creativecommons.org/share-your-work/)  
[Template PI FECAP](https://github.com/fecaphub/Template_PI)  
