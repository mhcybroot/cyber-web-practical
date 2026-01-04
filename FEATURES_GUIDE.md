# Cyberweb: Comprehensive Feature Guide

This guide provides a detailed breakdown of all the interactive, gamified, and educational features available within the Cyberweb platform.

---

## 🕶️ Visual & Interactive Overhauls

### 1. Matrix Mode
- **What it is**: A high-impact visual transformation that turns the UI into a classic "Matrix" terminal.
- **Key Elements**:
    - **Falling Code Rain**: A custom Canvas-based animation that runs at 60fps.
    - **CRT Scanlines**: A subtle overlay effect that mimics old-school monitors.
    - **VT323 Typography**: All fonts switch to a specialized terminal font.
- **How to trigger**: Use the **MATRIX** toggle switch in the top header.

### 2. Dark Web Mode
- **What it is**: A "sketchy" underground aesthetic designed for high-stakes monitoring.
- **Key Elements**:
    - High-contrast green-on-black color scheme.
    - Simplified, blocky UI components.
    - Unique "Hacker Cursor" effect.
- **How to trigger**: Click the **"Enter Dark Web"** button within the Chaos Control Center.

### 3. Mascot: "Buggy" (The AI Guide)
- **Personality**: Sarcastic, knowledgeable, and prone to "roasting" the user.
- **Dynamic Roasts**:
    - **Idle Roast**: If you sit still for 60 seconds, he'll mock your speed.
    - **Failure Roast**: Triggered by 403 errors or incorrect flag submissions.
- **Levelling System**: Buggy's outfit changes based on your success:
    - **Default**: No gear.
    - **White Hat**: Unlocked at 10 Chaos Points.
    - **Black Hat**: Unlocked at 25 Chaos Points.
    - **Tinfoil Hat**: Unlocked at 50 Chaos Points (Master Hacker status).

---

## 🎮 Gamification & Economy

### 4. The Chaos Score
- **Concept**: A global tracker for "destructive" but educational activities.
- **Earning Points**: Successfully exploiting vulnerabilities or submitting captured flags.
- **Display**: Shown in the header badge with a 💀 icon.

### 5. Chaos Miner (Satire)
- **What it is**: A background widget in the sidebar that "mines" fake cryptocurrency using non-existent CPU power.
- **Features**: 
    - Real-time "CPU Load" simulator.
    - Progress bar indicating coin generation.
- **Currency**: **Chaos Coins**.

### 6. Zero-Day Shop
- **Function**: A marketplace where you can spend Chaos Coins.
- **Items**: Currently offers **"Zero-Day Hints"** for captured flags to help you when you get stuck.

---

## ☣️ Vulnerability Modules (The Lab)

| # | Module | Description | Exploit Concept |
| :--- | :--- | :--- | :--- |
| **01** | **SQL Injection** | Unsanitized database queries. | Use `' OR '1'='1` to bypass login or extract data. |
| **02** | **Reflected XSS** | Scripts reflected off the URL/params. | Inject `<script>alert(1)</script>` into search bars. |
| **03** | **Stored XSS** | Scripts saved into the database permanently. | Leave a comment with a malicious payload. |
| **04** | **IDOR** | Insecure Direct Object Reference. | Change the `?id=1` parameter in the URL to view other profiles. |
| **05** | **Weak Crypto** | Poor hashing or encryption practices. | Observe how passwords are stored or handled in registration. |
| **06** | **CSRF** | Cross-Site Request Forgery. | Trigger an action on behalf of another user via a link. |
| **07** | **Data Exposure** | Leakage of sensitive API information. | Inspect network responses for hidden JSON fields. |
| **08** | **Sec. Misconfig** | Verbose error pages or debug modes. | Trigger a 500 error to see the stack trace. |
| **09** | **Open Redirect** | Unvalidated redirects to external sites. | Use `?url=http://malicious.com` to trick users. |
| **10** | **Rate Limiting** | Lack of protection against brute force. | Attempt to guess a password multiple times rapidly. |
| **11** | **SSRF** | Server-Side Request Forgery. | Trick the server into scanning its own internal network. |
| **12** | **SSTI (Mad Libs)**| Server-Side Template Injection. | Use SpEL payloads like `${7*7}` to execute code. |

---

## 🚨 Security Simulations

### 7. Fake Ransomware (WannaCry)
- **Trigger**: 5 consecutive failed login attempts on the same session.
- **Effect**: A full-screen "Your files are encrypted" lockdown appears.
- **Purpose**: To demonstrate the psychological and operational impact of a breach in a controlled environment.

### 8. Chaos Control Center
- **Function**: A real-time "Command and Control" dashboard.
- **Features**:
    - **Incident Feed**: A rolling log of simulated security events.
    - **Stability Metrics**: Visual gauges for tables dropped and firewall tears.
    - **3D Chaos Core**: A pulsing orb that reacts to system activity.

---

## 🛠️ Developer Interface

- **Flag Submission**: A dedicated input in the top bar to claim points.
- **Audio SFX Engine**: Retro 8-bit sounds for success, failure, and glitch events.
- **Persistent State**: Most features use `localStorage` to ensure your session remains intact after a refresh.
