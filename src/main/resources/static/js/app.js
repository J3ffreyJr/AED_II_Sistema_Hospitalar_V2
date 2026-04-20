const API_BASE = '/api';

async function fetchJSON(url, options = {}) {
    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        const text = await response.text();
        return text ? JSON.parse(text) : null;
    } catch (error) {
        console.error('Erro:', error);
        return null;
    }
}

function showMessage(text, type = 'info') {
    const msg = document.getElementById('message');
    msg.textContent = text;
    msg.className = `message ${type}`;
    setTimeout(() => { msg.className = 'message'; }, 3000);
}

async function atualizarEstatisticas() {
    const stats = await fetchJSON(`${API_BASE}/estatisticas`);
    if (stats) {
        document.getElementById('statNormal').textContent = stats.filaNormalSize;
        document.getElementById('statPrioritaria').textContent = stats.filaPrioritariaSize;
        document.getElementById('statAtendidos').textContent = stats.totalAtendidos;
        document.getElementById('statUndo').textContent = stats.operacoesUndo;
    }
}

async function renderizarListas() {
    const [filaNormal, filaPrioritaria, historico] = await Promise.all([
        fetchJSON(`${API_BASE}/pacientes`),
        fetchJSON(`${API_BASE}/pacientes/prioritarios`),
        fetchJSON(`${API_BASE}/pacientes/historico`)
    ]);

    renderLista('filaNormal', filaNormal || []);
    renderLista('filaPrioritaria', filaPrioritaria || [], true);
    renderLista('historico', historico || []);
}

function renderLista(id, pacientes, isPrioritaria = false) {
    const ul = document.getElementById(id);
    ul.innerHTML = '';
    if (pacientes.length === 0) {
        ul.innerHTML = '<li style="color:#666;font-style:italic;">Vazio</li>';
        return;
    }
    pacientes.forEach(p => {
        const li = document.createElement('li');
        li.className = isPrioritaria || p.idade >= 60 ? 'prioritario' : '';
        li.textContent = `#${p.id} - ${p.nome} (${p.idade} anos)`;
        ul.appendChild(li);
    });
}

document.getElementById('pacienteForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        nome: document.getElementById('nome').value,
        idade: parseInt(document.getElementById('idade').value),
        bi: document.getElementById('bi').value,
        telefone: document.getElementById('telefone').value,
        endereco: document.getElementById('endereco').value,
        prioridade: document.getElementById('prioridade').value
    };
    const result = await fetchJSON(`${API_BASE}/pacientes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    if (result) {
        showMessage(`Paciente ${result.nome} inserido com sucesso!`, 'success');
        document.getElementById('pacienteForm').reset();
        await Promise.all([atualizarEstatisticas(), renderizarListas()]);
    } else {
        showMessage('Erro ao inserir paciente', 'error');
    }
});

async function atenderProximo() {
    const result = await fetchJSON(`${API_BASE}/pacientes/proximo`);
    if (result) {
        showMessage(`Atendido: ${result.nome}`, 'success');
        await Promise.all([atualizarEstatisticas(), renderizarListas()]);
    } else {
        showMessage('Não há pacientes na fila', 'error');
    }
}

async function desfazer() {
    const result = await fetchJSON(`${API_BASE}/pacientes/desfazer`, { method: 'POST' });
    if (result) {
        showMessage(result.mensagem, result.mensagem.includes('sucesso') ? 'success' : 'error');
        await Promise.all([atualizarEstatisticas(), renderizarListas()]);
    }
}

async function buscarPaciente() {
    const id = prompt('Digite o ID do paciente:');
    if (!id) return;
    const result = await fetchJSON(`${API_BASE}/pacientes/${id}`);
    if (result) {
        showMessage(`Encontrado: #${result.id} - ${result.nome} (${result.idade} anos, ${result.prioridade})`, 'info');
    } else {
        showMessage('Paciente não encontrado', 'error');
    }
}

async function removerPaciente() {
    const id = prompt('Digite o ID do paciente a remover:');
    if (!id) return;
    await fetch(`${API_BASE}/pacientes/${id}`, { method: 'DELETE' });
    showMessage('Paciente removido', 'info');
    await Promise.all([atualizarEstatisticas(), renderizarListas()]);
}

atualizarEstatisticas();
renderizarListas();
