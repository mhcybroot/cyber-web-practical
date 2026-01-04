# Cyberweb: The Gamified Hacking Lab

Cyberweb is a modern, high-fidelity web application designed for cybersecurity education and vulnerability demonstration. It bridges the gap between CLI-based security tools and interactive learning through a rich, gamified experience.

## 🚀 Vision
Most capture-the-flag (CTF) platforms are sterile and text-heavy. Cyberweb changes the narrative by providing a "lived-in" hacker environment with high-end aesthetics, a reactive AI mascot, and visual feedback for security events.

---

## ✨ Key Features

### 🕶️ Visual Modes
- **Cyber-Dash**: A sleek glassmorphic dashboard tracking your "Chaos Score".
- **Matrix Mode**: A canvas-based code rain overhaul with CRT scanlines and terminal typography.
- **Dark Web Mode**: A gritty, retro-underground forum aesthetic for advanced monitoring.

### 🎭 The Human Element
- **Buggy the Mascot**: A resident AI persona that reacts to your actions, provides hints, and roasts your failed attempts.
- **Dynamic Skins**: Buggy's appearance evolves (White Hat to Tinfoil Hat) based on your progress.

### 💸 Chaos Economy
- **Crypto-Miner (Satire)**: A background "mining" operation that generates Chaos Coins.
- **Zero-Day Shop**: Spend your coins to purchase hints for difficult vulnerability flags.

### 🚨 Education via Feedback
- **Simulated Ransomware**: Failing login security triggers a harmless "WannaCry" style lockdown to demonstrate real-world impact.
- **Chaos Center**: A real-time incident feed monitoring simulated system instability.

---

## 🛠️ Technology Stack

- **Backend**: Java 21+ with Spring Boot 3.x
- **Security**: Spring Security 6.x
- **Database**: H2 (In-memory for easy demo deployment)
- **Frontend**: 
  - Thymeleaf Template Engine
  - Vanilla CSS (with modern variables & keyframe animations)
  - Vanilla JavaScript (Custom Audio/Visual Engines)
  - Canvas API (Matrix Rain)

---

## 🏗️ Getting Started

### Prerequisites
- JDK 21 or higher
- Gradle 8.x (included via wrapper)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/youruser/cyberweb.git
   cd cyberweb
   ```
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```
3. Access the lab:
   Open `http://localhost:8080` in your browser.
   - **Default User**: `user` / `password`
   - **Default Admin**: `admin` / `admin`

---

## ☣️ Vulnerability Modules
Cyberweb includes modules for the following security concepts:
1. **SQL Injection**: Multi-vector bypass and data extraction.
2. **XSS (Stored & Reflected)**: Script execution and session hijacking.
3. **SSTI (SpEL)**: Server-Side Template Injection via Spring Expression Language.
4. **CSRF**: Cross-Site Request Forgery via state-changing forms.
5. **SSRF**: Server-Side Request Forgery & Internal Network Scanning.
6. **IDOR**: Insecure Direct Object References in user profiles.
7. **Security Misconfiguration**: Debug error leak exploitation.

---

## 📜 License
Distributed under the MIT License. See `LICENSE` for more information.

---

## 🤝 Contact
Your Name - your.email@example.com
Project Link: [https://github.com/youruser/cyberweb](https://github.com/youruser/cyberweb)
