#  Sistema de Gestão Hospitalar

> Trabalho Prático de Estruturas de Dados — Java + Spring Boot

Sistema web de gestão de atendimento hospitalar que demonstra a
aplicação prática de estruturas de dados lineares e tabela de
dispersão, implementadas manualmente em Java.

---

##  Estruturas de Dados Implementadas

| Estrutura                   | Uso no Sistema                       | Complexidade           |
|-----------------------------|--------------------------------------|------------------------|
| **Fila (FIFO)**             | Atendimento de pacientes normais     | O(1) insert/remove     |
| **Heap de Prioridade**      | Atendimento prioritário (idosos ≥60) | O(log n) insert/remove |
| **Lista Duplamente Ligada** | Histórico de atendimentos            | O(1) insert            |
| **Pilha (LIFO) ×2**         | Desfazer e Refazer operações         | O(1) push/pop          |
| **Tabela Hash**             | Pesquisa rápida de pacientes por ID  | O(1) médio             |

---

##  Tecnologias

- Java
- Spring Boot
- Maven
- HTML + CSS + JavaScript (interface web)

---

##  Como executar ???

**Pré-requisitos:** Java (Versao 17+ De preferência) e Maven instalados.

```bash
# Clonar o repositório
git clone https://github.com/SEU_USUARIO/sistema-hospitalar-web.git

# Entrar na pasta
cd sistema-hospitalar-web

# Executar
mvn spring-boot:run
```

Aceder em: [http://localhost:8080](http://localhost:8080)

---

## Endpoints da API

| Método  | Endpoint                      | Descrição               |
|---------|-------------------------------|-------------------------|
| GET     | `/api/pacientes`              | Lista fila normal       |
| GET     | `/api/pacientes/prioritarios` | Lista fila prioritária  |
| POST    | `/api/pacientes`              | Insere novo paciente    |
| GET     | `/api/pacientes/proximo`      | Atende o próximo        |
| DELETE  | `/api/pacientes/{id}`         | Remove paciente         |
| POST    | `/api/pacientes/desfazer`     | Desfaz última operação  |
| POST    | `/api/pacientes/refazer`      | Refaz última operação   |
| GET     | `/api/pacientes/historico`    | Lista histórico         |
| GET     | `/api/pacientes/estatisticas` | Estatísticas do sistema |

---

## Grupo

| Nome             | Função                   |
|------------------|--------------------------|
| Jeffrey + Guerco | Estruturas lineares      |
| Jeffrey          | Tabela Hash              |
| Jeffrey          | API REST + Interface web |

---

## 📚 Disciplina

Estruturas de Dados II — [ISUTC] — Abril 2026
Jeffrey William II
