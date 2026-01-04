# Cyberweb Technical Documentation

## 1. Project Organization
The project follows a standard Spring Boot structure:
- `src/main/java/root/cyb/mh/cyberweb/controller`: Handles routing and vulnerability logic.
- `src/main/resources/templates`: Specialized Thymeleaf templates for each module.
- `src/main/resources/static`: 
    - `css`: Modular style sheets (e.g., `matrix.css`, `chaos-center.css`).
    - `js`: `demo.js` - The core engine for visual/audio FX and game state.

## 2. Core Systems

### A. The Visual Engine (`demo.js`)
The visual engine handles the "feeling" of the lab:
- **Matrix Rain**: Uses a 60fps high-performance loop on a dedicated canvas.
- **Glitch FX**: Randomized CSS transforms triggered on security events.
- **CRT Scanlines**: Overlay layers and SVG filters to simulate old hardware.

### B. The Mascot Controller
Buggy is controlled via a state-based system:
- **Idle Roasts**: Triggered by a 60-second inactivity listener.
- **Contextual Responses**: Mapped to achievement triggers and HTTP error codes.
- **Skin Manager**: Dynamically swaps CSS classes based on `localStorage` game progress.

### C. Chaos Economics
A simple satirical system implemented in JS:
- **Chaos Coins**: Persistence via `localStorage`.
- **The Miner**: An interval-based generator that simulates CPU load.

## 3. Vulnerability Implementation Guide

### Adding a New Vulnerability
1. Create a new `Controller` in the `controller` package.
2. Define a GET route for the UI and a POST route for the exploitation logic.
3. Add a corresponding `.html` file in `templates/vuln/`.
4. Register the new module in `fragments/layout.html` sidebar.
5. (Optional) Add achievement triggers in `demo.js`.

---

## 4. Security Philosophy
Cyberweb is **intentionally vulnerable**. 
> [!WARNING]
> DO NOT deploy this application to a public server without a robust firewall or proxy layer. It is designed to be exploited and can be used to gain access to the host machine if misconfigured.

### Key Vulnerability: SSTI
Implemented using `SpelExpressionParser`. This demonstrates how template evaluation can lead to Remote Code Execution (RCE).
Example Payload: `${T(java.lang.Runtime).getRuntime().exec('id')}`

---

## 5. Persistence
State is managed through a hybrid approach:
- **User/Admin Accounts**: Persistent H2/JPA.
- **CTF Progress**: `localStorage` (for non-authenticated flair).
- **Achievements**: `localStorage`.
