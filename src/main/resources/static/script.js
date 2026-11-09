const display = document.getElementById('display');
const status = document.getElementById('status');
const historyDiv = document.getElementById('history');
const calculateButton = document.querySelector('[data-action="calculate"]');
const storageKey = 'atelier-calcul-history';
let history = loadHistory();
let revision = 0;
let pending = false;

function loadHistory() {
    try {
        const saved = JSON.parse(localStorage.getItem(storageKey));
        return Array.isArray(saved) ? saved.filter(item => item && typeof item.expression === 'string'
            && item.expression.length <= 512 && typeof item.result === 'number'
            && Number.isFinite(item.result)).slice(0, 10) : [];
    } catch {
        return [];
    }
}

function setStatus(message, error = false) {
    status.textContent = message;
    status.classList.toggle('error', error);
}

function changed() {
    revision++;
    setStatus('À vous de jouer.');
}

function insert(value) {
    const start = display.selectionStart ?? display.value.length;
    const end = display.selectionEnd ?? start;
    display.setRangeText(value, start, end, 'end');
    display.focus();
    changed();
}

function clearDisplay() {
    display.value = '';
    changed();
    display.focus();
}

function deleteLastChar() {
    const start = display.selectionStart ?? display.value.length;
    const end = display.selectionEnd ?? start;
    display.setRangeText('', start === end ? Math.max(0, start - 1) : start, end, 'end');
    display.focus();
    changed();
}

function saveHistory() {
    try {
        localStorage.setItem(storageKey, JSON.stringify(history));
    } catch {
        setStatus('Calcul conservé pour cette session uniquement.');
    }
}

function renderHistory() {
    historyDiv.replaceChildren();
    document.getElementById('history-count').textContent = String(history.length).padStart(2, '0');
    document.getElementById('clear-history').disabled = history.length === 0;
    if (!history.length) {
        const empty = document.createElement('p');
        empty.className = 'empty-history';
        empty.textContent = 'Chaque idée commence par un premier calcul.';
        const hint = document.createElement('span');
        hint.textContent = 'Vos résultats apparaîtront ici.';
        empty.append(hint);
        historyDiv.append(empty);
        return;
    }
    for (const item of history) {
        const button = document.createElement('button');
        button.className = 'history-item';
        button.title = 'Réutiliser ce résultat';
        const expression = document.createElement('span');
        expression.textContent = item.expression;
        const result = document.createElement('strong');
        result.textContent = '= ' + item.result;
        button.append(expression, result);
        button.addEventListener('click', () => {
            display.value = String(item.result);
            display.focus();
            display.setSelectionRange(display.value.length, display.value.length);
            changed();
        });
        historyDiv.append(button);
    }
}

async function calculate() {
    if (pending) return;
    const expression = display.value.trim();
    if (!expression) {
        setStatus('Saisissez une expression pour commencer.', true);
        return;
    }
    const submittedRevision = revision;
    pending = true;
    calculateButton.disabled = true;
    setStatus('Calcul en cours…');
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), 10000);
    try {
        const response = await fetch('/api/calculator/calculate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ expression }),
            signal: controller.signal
        });
        if (!response.ok) throw new Error('Le serveur ne peut pas répondre pour le moment.');
        const data = await response.json();
        if (data.error) throw new Error(data.error);
        if (typeof data.result !== 'number' || !Number.isFinite(data.result)) {
            throw new Error('Le résultat ne peut pas être représenté.');
        }
        history.unshift({ expression, result: data.result });
        history = history.slice(0, 10);
        renderHistory();
        if (revision === submittedRevision) {
            display.value = String(data.result);
            display.setSelectionRange(display.value.length, display.value.length);
            setStatus('Résultat calculé · trigonométrie en degrés.');
        }
        saveHistory();
    } catch (error) {
        if (revision === submittedRevision) {
            setStatus(error.name === 'AbortError' ? 'Délai dépassé. Réessayez.'
                : error instanceof TypeError ? 'Serveur inaccessible. Vérifiez que l’application est lancée.'
                : error.message, true);
        }
    } finally {
        clearTimeout(timeout);
        pending = false;
        calculateButton.disabled = false;
    }
}

document.querySelectorAll('[data-value]').forEach(button => {
    button.addEventListener('click', () => insert(button.dataset.value));
});
document.querySelector('[data-action="clear"]').addEventListener('click', clearDisplay);
document.querySelector('[data-action="delete"]').addEventListener('click', deleteLastChar);
calculateButton.addEventListener('click', calculate);
document.getElementById('clear-history').addEventListener('click', () => {
    history = [];
    saveHistory();
    renderHistory();
});
display.addEventListener('input', changed);
document.addEventListener('keydown', event => {
    if (event.ctrlKey || event.metaKey || event.altKey) return;
    if (event.key === 'Escape') {
        event.preventDefault();
        clearDisplay();
    } else if (event.key === 'Enter' && (event.target === display || event.target === document.body)) {
        event.preventDefault();
        calculate();
    } else if (event.target === document.body && /^[0-9.+*/()^\-]$/.test(event.key)) {
        event.preventDefault();
        insert(event.key);
    }
});
renderHistory();
