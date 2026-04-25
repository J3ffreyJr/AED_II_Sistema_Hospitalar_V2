const API_BASE = '/api';

async function fetchJSON(url, options = {}) {
    try {
        const response = await fetch(url, options);
        const text = await response.text();
        if (!response.ok) {
            const message = text || response.statusText || `HTTP ${response.status}`;
            throw new Error(message);
        }
        if (!text) {
            return null;
        }
        try {
            return JSON.parse(text);
        } catch (parseError) {
            console.error('Falha ao parsear JSON:', text, parseError);
            return null;
        }
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
        // Total de pacientes = fila normal + fila prioritária
        const totalPacientes = stats.filaNormalSize + stats.filaPrioritariaSize;
        document.getElementById('statTotal').textContent = totalPacientes;
    }
}

async function renderizarListas() {
    const [filaNormal, filaPrioritaria, historico] = await Promise.all([
        fetchJSON(`${API_BASE}/pacientes`),
        fetchJSON(`${API_BASE}/pacientes/prioritarios`),
        fetchJSON(`${API_BASE}/pacientes/historico`)
    ]);

    if (filaNormal) renderLista('filaNormal', filaNormal);
    if (filaPrioritaria) renderLista('filaPrioritaria', filaPrioritaria, true);
    if (historico) {
        selectionSortPacientes(historico);
        renderLista('historico', historico);
    }
}

function selectionSortPacientes(pacientes) {
    for (let i = 0; i < pacientes.length - 1; i++) {
        let minIndex = i;
        for (let j = i + 1; j < pacientes.length; j++) {
            if (pacientes[j].id < pacientes[minIndex].id) {
                minIndex = j;
            }
        }
        if (minIndex !== i) {
            const temp = pacientes[i];
            pacientes[i] = pacientes[minIndex];
            pacientes[minIndex] = temp;
        }
    }
}

function renderLista(id, pacientes, isPrioritaria = false) {
    const ul = document.getElementById(id);
    ul.innerHTML = '';
    if (!pacientes || pacientes.length === 0) {
        ul.innerHTML = '<li style="color:#666;font-style:italic;">Vazio</li>';
        return;
    }
    pacientes.forEach(p => {
        const li = document.createElement('li');
        li.className = (isPrioritaria || p.idade >= 60) ? 'prioritario' : '';
        let text = `#${p.id} - ${p.nome} (${p.idade} anos)`;
        if (p.removido) {
            text += ' [REMOVIDO]';
            li.style.color = '#999';
            li.style.textDecoration = 'line-through';
        }
        li.textContent = text;
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

async function refazer() {
    const result = await fetchJSON(`${API_BASE}/pacientes/refazer`, { method: 'POST' });
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
    try {
        const response = await fetch(`${API_BASE}/pacientes/${id}`, { method: 'DELETE' });
        if (response.ok) {
            showMessage('Paciente removido da fila e histórico', 'success');
        } else if (response.status === 404) {
            showMessage('Paciente não encontrado na fila', 'error');
        } else {
            showMessage('Erro ao remover paciente', 'error');
        }
    } catch (error) {
        showMessage('Erro ao remover paciente', 'error');
    }
    await Promise.all([atualizarEstatisticas(), renderizarListas()]);
}

async function limparHistorico() {
    const result = await fetchJSON(`${API_BASE}/pacientes/historico`, { method: 'DELETE' });
    if (result && result.sucesso) {
        showMessage(result.mensagem || 'Histórico limpo com sucesso', 'success');
        await Promise.all([atualizarEstatisticas(), renderizarListas()]);
        return;
    }
    showMessage('Erro ao limpar histórico', 'error');
}

atualizarEstatisticas();
renderizarListas();
