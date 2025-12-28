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

function triggerMascot(text, mood = 'neutral') {
    const container = document.getElementById('mascot-container');
    const bubble = document.getElementById('mascot-text');
    if (!container || !bubble) return;

    bubble.innerText = text;
    container.classList.add('visible');
    container.classList.add('speaking');

    // Auto-hide
    clearTimeout(mascotTimeout);
    mascotTimeout = setTimeout(() => {
        container.classList.remove('speaking');
        setTimeout(() => container.classList.remove('visible'), 300);
    }, 4000);
}

// --- Visual FX Engine ---

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
            triggerMascot(result.message, 'snarky');
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

// Init
document.addEventListener('DOMContentLoaded', updateScoreUI);
