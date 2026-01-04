/**
 * CyberLab Demo Helpers
 */

// --- Audio Engine (Web Audio API) ---
const AudioContext = window.AudioContext || window.webkitAudioContext;
const audioCtx = new AudioContext();

function playTone(freq, type, duration) {
    if (audioCtx.state === 'suspended') audioCtx.resume();
    const osc = audioCtx.createOscillator();
    const gain = audioCtx.createGain();
    osc.type = type;
    osc.frequency.setValueAtTime(freq, audioCtx.currentTime);
    gain.gain.setValueAtTime(0.1, audioCtx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, audioCtx.currentTime + duration);
    osc.connect(gain);
    gain.connect(audioCtx.destination);
    osc.start();
    osc.stop(audioCtx.currentTime + duration);
}

function playNoise(duration) {
    if (audioCtx.state === 'suspended') audioCtx.resume();
    const bufferSize = audioCtx.sampleRate * duration;
    const buffer = audioCtx.createBuffer(1, bufferSize, audioCtx.sampleRate);
    const data = buffer.getChannelData(0);
    for (let i = 0; i < bufferSize; i++) {
        data[i] = Math.random() * 2 - 1;
    }
    const noise = audioCtx.createBufferSource();
    noise.buffer = buffer;
    const gain = audioCtx.createGain();
    gain.gain.setValueAtTime(0.1, audioCtx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, audioCtx.currentTime + duration);
    noise.connect(gain);
    gain.connect(audioCtx.destination);
    noise.start();
}

const SFX = {
    success: () => {
        playTone(600, 'square', 0.1);
        setTimeout(() => playTone(800, 'square', 0.2), 100);
    },
    fail: () => {
        playTone(150, 'sawtooth', 0.3);
        setTimeout(() => playTone(100, 'sawtooth', 0.5), 150);
    },
    glitch: () => {
        playNoise(0.3);
    },
    hacking: () => {
        playTone(1200, 'sine', 0.05);
    }
};

// --- Mascot Controller ---

let mascotTimeout;
let idleTimer;

function resetIdleTimer() {
    clearTimeout(idleTimer);
    idleTimer = setTimeout(() => {
        triggerMascot(getRandomQuote('idle'), 'snarky');
    }, 60000); // 1 minute
}

document.addEventListener('mousemove', resetIdleTimer);
document.addEventListener('keypress', resetIdleTimer);

function getMascotSkin() {
    const score = parseInt(localStorage.getItem('chaosScore') || '0');
    if (score >= 50) return 'tinfoil-hat';
    if (score >= 25) return 'black-hat';
    if (score >= 10) return 'white-hat';
    return 'default';
}

function triggerMascot(text, mood = 'neutral') {
    const container = document.getElementById('mascot-container');
    const bubble = document.getElementById('mascot-text');
    if (!container || !bubble) return;

    // Apply skin
    container.className = ''; // Reset
    container.id = 'mascot-container';
    container.classList.add('visible');
    container.classList.add(getMascotSkin());

    bubble.innerText = text;
    container.classList.add('speaking');

    // Auto-hide
    clearTimeout(mascotTimeout);
    mascotTimeout = setTimeout(() => {
        container.classList.remove('speaking');
        setTimeout(() => container.classList.remove('visible'), 300);
    }, 4000);
}

// --- Visual FX Engine ---

let matrixInterval;
function initMatrixRain() {
    const canvas = document.getElementById('matrix-canvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$+-*/=%\"'<>!&|^~?@#";
    const fontSize = 16;
    const columns = canvas.width / fontSize;
    const drops = [];

    for (let x = 0; x < columns; x++) {
        drops[x] = 1;
    }

    function draw() {
        ctx.fillStyle = "rgba(0, 0, 0, 0.05)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        ctx.fillStyle = "#0F0";
        ctx.font = fontSize + "px VT323";

        for (let i = 0; i < drops.length; i++) {
            const text = chars.charAt(Math.floor(Math.random() * chars.length));
            ctx.fillText(text, i * fontSize, drops[i] * fontSize);

            if (drops[i] * fontSize > canvas.height && Math.random() > 0.975) {
                drops[i] = 0;
            }
            drops[i]++;
        }
    }

    if (matrixInterval) clearInterval(matrixInterval);
    matrixInterval = setInterval(draw, 33);
}

function toggleMatrixMode() {
    const isChecked = document.getElementById('matrixToggle').checked;
    if (isChecked) {
        document.body.classList.add('matrix-mode');
        localStorage.setItem('matrixMode', 'enabled');
        initMatrixRain();
        triggerMascot("Follow the white rabbit...", "neutral");
    } else {
        document.body.classList.remove('matrix-mode');
        localStorage.setItem('matrixMode', 'disabled');
        if (matrixInterval) clearInterval(matrixInterval);
        triggerMascot("Back to the boring world.", "neutral");
    }
}

function restoreMatrixMode() {
    const state = localStorage.getItem('matrixMode');
    const toggle = document.getElementById('matrixToggle');
    if (state === 'enabled' && toggle) {
        toggle.checked = true;
        document.body.classList.add('matrix-mode');
        initMatrixRain();
    }
}

function triggerGlitch(elementId = null) {
    const target = elementId ? document.getElementById(elementId) : document.body;
    if (!target) return;

    target.classList.add('glitch-active');
    document.body.classList.add('body-shake');

    setTimeout(() => {
        target.classList.remove('glitch-active');
        document.body.classList.remove('body-shake');
    }, 500);
}

function triggerConfetti() {
    // Simple custom confetti
    const colors = ['#f00', '#0f0', '#00f', '#ff0', '#0ff', '#f0f'];
    for (let i = 0; i < 50; i++) {
        const particle = document.createElement('div');
        particle.classList.add('confetti-particle');
        particle.style.left = Math.random() * 100 + 'vw';
        particle.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
        particle.style.animationDuration = (Math.random() * 2 + 1) + 's';
        document.body.appendChild(particle);
        setTimeout(() => particle.remove(), 3000);
    }
}

// --- Gamification ---

let coinInterval;
function initMiner() {
    let coins = parseInt(localStorage.getItem('chaosCoins') || '0');
    let progress = 0;
    const progressEl = document.getElementById('miner-progress');
    const coinsEl = document.getElementById('chaos-coins');
    const loadEl = document.getElementById('miner-load');

    if (!progressEl || !coinsEl) return;

    coinsEl.innerText = coins;

    if (coinInterval) clearInterval(coinInterval);
    coinInterval = setInterval(() => {
        progress += 10;
        if (progress > 100) {
            progress = 0;
            coins += 1;
            localStorage.setItem('chaosCoins', coins);
            coinsEl.innerText = coins;
            // Fake load spike
            loadEl.innerText = (90 + Math.floor(Math.random() * 10)) + "%";
            loadEl.classList.toggle('text-red');
        }
        progressEl.style.width = progress + "%";
    }, 1000);
}

function openShop() {
    const coins = parseInt(localStorage.getItem('chaosCoins') || '0');
    const hintCost = 10;

    if (coins < hintCost) {
        triggerMascot(`You only have ${coins} Chaos Coins. Mining is hard work, kid. Try harder.`, 'snarky');
        return;
    }

    const buy = confirm(`Zero-Day Shop\n\n- Flag Hint (Costs ${hintCost} Coins)\n\nSpend ${hintCost} coins for a hint?`);
    if (buy) {
        localStorage.setItem('chaosCoins', coins - hintCost);
        document.getElementById('chaos-coins').innerText = coins - hintCost;
        SFX.success();
        notifyAchievement("Dirty Money spent");
        alert("HINT: Try looking at the source code of the grievance form. SpEL evaluation can be dangerous!");
    }
}

function updateScoreUI() {
    const scoreEl = document.getElementById('chaosScoredisplay');
    if (scoreEl) {
        scoreEl.innerText = localStorage.getItem('chaosScore') || '0';
    }
}

function notifyAchievement(name) {
    const toast = document.createElement('div');
    toast.style.position = 'fixed';
    toast.style.bottom = '20px';
    toast.style.right = '20px';
    toast.style.background = '#000';
    toast.style.border = '1px solid var(--accent-cyan)';
    toast.style.color = '#fff';
    toast.style.padding = '15px';
    toast.style.zIndex = '10000';
    toast.innerHTML = `<strong style="color:var(--accent-cyan)">🏆 Achievement Unlocked</strong><br>${name}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
}

// --- Wrappers ---

function runDemo(inputName, payload) {
    const input = document.querySelector(`[name="${inputName}"]`);
    if (input) {
        input.value = payload;

        // Visual & Audio FX based on context
        if (payload.includes('SCRIPT') || payload.includes('OR') || payload === 'admin') {
            triggerGlitch(); // Chaos!
            SFX.glitch();
            updateChaosScore(5);
            triggerMascot(getRandomQuote('hack'), 'excited');
        } else if (payload === 'wrong' || payload.includes('fail')) {
            SFX.fail();
            triggerMascot("Wrong password. Try 'admin' if you dare.", 'snarky');
        } else {
            // Normal interaction
            SFX.hacking();
            input.style.transition = "background-color 0.3s";
            input.style.backgroundColor = "rgba(0, 243, 255, 0.2)";
            setTimeout(() => input.style.backgroundColor = "", 300);
        }
    }
}

// --- CTF Logic ---

async function submitFlag() {
    const input = document.getElementById('flagInput');
    if (!input || !input.value) return;

    const flag = input.value.trim();

    // Check if already claimed
    const claimedFlags = JSON.parse(localStorage.getItem('claimedFlags') || '[]');
    if (claimedFlags.includes(flag)) {
        triggerMascot("You already claimed that one! Greedy.", "neutral");
        return;
    }

    try {
        const response = await fetch('/api/flags/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Add CSRF if enabled, but for this demo might be skipped or need meta tag
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
            },
            body: JSON.stringify({ flag: flag })
        });

        const result = await response.json();

        if (result.valid) {
            SFX.success();
            triggerConfetti();
            updateChaosScore(result.points);
            triggerMascot(result.message, 'excited');

            // Save as claimed
            claimedFlags.push(flag);
            localStorage.setItem('claimedFlags', JSON.stringify(claimedFlags));
            input.value = '';
            input.placeholder = "Flag Secured!";
            setTimeout(() => input.placeholder = "Enter captured flag...", 3000);
        } else {
            SFX.fail();
            triggerMascot(getRandomQuote('fail'), 'snarky');
            input.classList.add('glitch-active');
            setTimeout(() => input.classList.remove('glitch-active'), 500);
        }

    } catch (e) {
        console.error(e);
        triggerMascot("Error contacting flag server.", "neutral");
    }
}

// Quotes
const QUOTES = {
    hack: ["Boom! Database gone!", "I love the smell of dropped tables!", "Sanitization? Never heard of her.", "CHAOS REIGNS!"],
    secure: ["Booo! Secure code is boring.", "Safe... but sleepy.", "You call that hacking?", "Where's the kaboom?"]
};

function getRandomQuote(type) {
    const list = QUOTES[type];
    return list[Math.floor(Math.random() * list.length)];
}

function updateChaosScore(points = 1) {
    let score = parseInt(localStorage.getItem('chaosScore') || '0');
    score += points;
    localStorage.setItem('chaosScore', score);
    updateScoreUI();

    // Check achievements
    if (score === 1) { notifyAchievement("First Blood"); SFX.success(); }
    if (score === 10) { notifyAchievement("Script Kiddie"); SFX.success(); }
    if (score === 25) { triggerMascot("You're getting good at this...", "suspicious"); }
    if (score === 50) { notifyAchievement("Master Hacker"); SFX.success(); }
}

function runDemoAndSubmit(inputName, payload) {
    runDemo(inputName, payload);
    const form = document.querySelector(`[name="${inputName}"]`).closest('form');
    if (form) {
        setTimeout(() => {
            form.submit();
        }, 800);
    }
}

function navigateTo(url) {
    window.location.href = url;
}

function checkFailedLogins() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        let fails = parseInt(localStorage.getItem('failedLogins') || '0');
        fails++;
        localStorage.setItem('failedLogins', fails);

        if (fails >= 5) {
            showRansomware();
        } else {
            triggerMascot(`Failed login attempt ${fails}/5. Keep going, you're almost... fired.`, 'snarky');
        }
    }
}

function showRansomware() {
    const overlay = document.getElementById('ransomware-overlay');
    if (overlay) {
        overlay.style.display = 'block';
        SFX.fail();
        // Start a fake timer
        let timeLeft = 3 * 3600; // 3 hours
        const timerEl = document.getElementById('ransom-timer');
        const timerInterval = setInterval(() => {
            timeLeft--;
            if (timeLeft <= 0) clearInterval(timerInterval);
            const h = Math.floor(timeLeft / 3600);
            const m = Math.floor((timeLeft % 3600) / 60);
            const s = timeLeft % 60;
            if (timerEl) timerEl.innerText = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
        }, 1000);
    }
}

function resetRansomware() {
    localStorage.setItem('failedLogins', '0');
    const overlay = document.getElementById('ransomware-overlay');
    if (overlay) overlay.style.display = 'none';
    triggerMascot("That was a close one, wasn't it?", "neutral");
}

// --- Chaos Control Center ---

function initChaosCenter() {
    const feed = document.getElementById('event-feed');
    if (!feed) return;

    const events = [
        "Unauthorized access attempt from 127.0.0.1 (Wait, that's me).",
        "Dropped table 'users'... just kidding. Or am I?",
        "Firewall reached critical sadness level.",
        "Detected logic bomb: 'if(true) launch_nukes()'.",
        "Sensitive data leaked to local printer.",
        "Hacker 'Buggy' connected to port 1337.",
        "Buffer overflow near your left elbow.",
        "XSS payload filtered... through a coffee filter."
    ];

    setInterval(() => {
        const item = document.createElement('div');
        item.className = 'event-item';
        const time = new Date().toLocaleTimeString();
        item.innerText = `[${time}] ${events[Math.floor(Math.random() * events.length)]}`;
        feed.prepend(item);
        if (feed.children.length > 10) feed.removeChild(feed.lastChild);

        // Randomly update metrics
        document.getElementById('metric-instability').innerText = (Math.random() * 10).toFixed(2) + "g";
        document.getElementById('metric-leaks').innerText = Math.floor(Math.random() * 500);
    }, 3000);
}

function enterDarkWeb() {
    document.body.classList.toggle('dark-web-mode');
    if (document.body.classList.contains('dark-web-mode')) {
        triggerMascot("Welcome to the underground. Don't touch anything.", "snarky");
    } else {
        triggerMascot("Back to the light. Boring.", "neutral");
    }
}

// Init
document.addEventListener('DOMContentLoaded', () => {
    updateScoreUI();
    restoreMatrixMode();
    initMiner();
    checkFailedLogins();
    initChaosCenter();
});
