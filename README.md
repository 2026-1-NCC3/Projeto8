<div align="center">
<h1>Lumière | Distributed Healthcare System</h1>
<img height="240" alt="distributed-healthcare-system" src="https://github.com/user-attachments/assets/5c05d1f9-608d-4f01-b244-5d76b7f9ede1" />

<p>Access the application: <a href="https://admin-lumiere.vercel.app">https://admin-lumiere.vercel.app</a></p>

<p>Group: <a href="https://github.com/AnaliceCoimbra/">Analice Carneiro</a>, <a href="https://github.com/alicelobwp">Mariah Alice Pereira</a>, <a href="https://github.com/sofiahernandes">Sofia Hernandes</a>, <a href="https://github.com/viick04">Victória Azevedo</a> <br/>
Advisors: <a href="https://www.linkedin.com/in/katia-bossi">Kátia Bossi</a>, <a href="https://www.linkedin.com/in/marco-aurelio-lima-barbosa">Marco Aurélio Barbosa</a>, <a href="https://www.linkedin.com/in/victorbarq">Victor Bruno de Quiroz</a>, <a href="https://www.linkedin.com/in/rodrigo-da-rosa-phd/">Rodrigo da Rosa</a></p>
</div>

<br/>

## Description

The **Lumière** project is a digital solution developed to support physiotherapist Maya Yoshiko Yamamoto, who specializes in Global Postural Reeducation (GPR), in managing and monitoring her patients.
Currently, part of the communication and therapeutic follow-up is carried out through messages and informal records, making it difficult to organize clinical information, track patient progress, and plan home exercise programs.
To address this problem, the project proposes the development of a system consisting of:
* **Mobile Application (Patient):** A mobile app where patients can access their prescribed exercises, watch instructional videos, record the completion of their activities, and monitor their progress throughout their treatment.
* **Web Module (Admin):** A web interface used by the physiotherapist to manage patients, medical records, prescribed exercises, and monitor treatment progress.
* **Backend (shared by the Mobile and Web applications) and Database:** Responsible for authentication, business logic, data storage, and integration between the mobile application and the web module.

<br/>

## Folders structure (inside /src)

```yaml
backend/
├─ MayaFisioLumiere/src/main/java/com/example/MayaFisioLumiere
  ├─ Configurations/
  ├─ Controller/
  ├─ Domain/
  ├─ Entity/
  ├─ Repository/
  ├─ Services/
  └─ MayaFisioLumiereApplication.java

frontend-app/
├─ app/src/main/
  ├─ AndroidManifest.xml
  └─ java/com/example/projeto8/
    ├─ UI
    ├─ adapter
    ├─ api
    ├─ model
    ├─ remote
  └─ res/
    ├─ anim
    ├─ drawable
    ├─ font
    ├─ layout
    └─ values

frontend-web/
├─ app/
  ├─ (pages)
  ├─ actions
  ├─ components
  ├─ hooks
  └─ lib
```

<br/>

## Instalation

- **Android**: download `app-debug.apk` on your Android device and follow the instructions.
- **Web App**: there is no instalation! Access the web application (which is hosted with mock-data [here](https://admin-lumiere.vercel.app/), since credits ran out - but you can use your local instance or host the app yourself).

<br/>

## How to run it locally

0. Pre-requisites: Android Studio, Visual Studio Code (or equivalent), Java (JDK v21), Node.js (v18^), npm/yarn, and Git

1. Run the mobile application locally  
a) Go to the frontend-app folder  
b) Open it in Android Studio
```bash
git clone https://github.com/sofiahernandes/distributed-healthcare-system.git
cd distributed-healthcare-system
cd frontend-app
```

2. Run the admin panel locally  
a) Go to the frontend-web folder and install dependencies  
b) Configure the environment variables in a .env file  
c) Execute the development server and access it in http://localhost:3000  
```bash
cd distributed-healthcare-system
cd frontend-web
npm install

NEXT_PUBLIC_API_URL=http://localhost:8080 # or your hosted URL

npm run dev
```

<br/>

## License

<a href="https://www.fecap.br">FECAP - Fundação de Comércio Álvares Penteado</a> - <a href="https://github.com/sofiahernandes/distributed-healthcare-system">Lumière (Distributed Healthcare System)</a> © 2026 by <a href="https://github.com/analicecoimbra">Analice Coimbra Carneiro</a>, <a href="https://github.com/alicelobwp">Mariah Alice Pimentel Lôbo Pereira</a>, <a href="https://github.com/sofiahernandes">Sofia Botechia Hernandes</a> and <a href="https://github.com/viick04">Victória Duarte Vieira Azevedo</a> is licensed under <a href="https://creativecommons.org/licenses/by-sa/4.0/">CC BY-SA 4.0</a> <img src="https://mirrors.creativecommons.org/presskit/icons/cc.svg" height="20" width="20" style="margin-left: 0.2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/by.svg" height="20" width="20" style="margin-left: 0.2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/sa.svg" height="20" width="20" style="margin-left: 0.2em;">
