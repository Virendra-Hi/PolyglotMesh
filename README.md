# PolyglotMesh 🚀
*A Secure, Sandboxed Multi-Language Code Execution Engine*

## 📌 About the Project
**PolyglotMesh** is an advanced full-stack application designed to execute multi-language code snippets securely within an isolated JVM memory sandbox using GraalVM. It bridges a Spring Boot reactive backend with a modern React & Monaco Editor frontend.

## 🛠️ Tech Stack
* **Backend:** Java 17+, Spring Boot, GraalVM Polyglot SDK (Python, JavaScript support)
* **Frontend:** React, Vite, Microsoft Monaco Editor
* **Security:** Strict GraalVM Context Sandboxing (`allowIO(false)`, `allowHostAccess(NONE)`)

## ✨ Key Features
* **Multi-Language Support:** Execute Python and JavaScript code snippets interactively.
* **Security & Isolation:** Prevents guest code from accessing the host file system, network, or OS processes.
* **Modern IDE Interface:** Split-pane console output and syntax highlighting powered by Monaco Editor.

## 🚀 Getting Started
1. Clone the repository:
   ```bash
   git clone [https://github.com/Virendra-Hi/PolyglotMesh.git](https://github.com/Virendra-Hi/PolyglotMesh.git)