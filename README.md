# Frequence!

Aplicação para registrar e consultar treinos, desenvolvida para as disciplinas de Front-end e Programação Web da SPTech.

## Funcionalidades

- Cadastro de treinos.
- Consulta do histórico.
- Exibição da quantidade de treinos e dos minutos acumulados.
- Limite de um check-in por dia.
- Cadastro de treinos passados ou no horário atual.

## Tecnologias

- React e Vite
- JavaScript e CSS Modules
- Java 21 e Spring Boot
- JdbcTemplate
- Banco de dados H2

## Estrutura

```text
individual-2/
├── back-end/
├── front-end/
└── README.md
```

## Pré-requisitos

- Java 21
- Maven
- Node.js compatível com Vite 8 
- npm
- Git

## Como executar

Clone o repositório:

```bash
git clone url do repo
cd individual-2
```

### Backend

Executar a classe `Application` pela IDE.

A API estará disponível em `http://localhost:8080`.

### Frontend

Em outro terminal, a partir da pasta principal:

```bash
cd front-end
npm install
npm run dev
```

Acesse `http://localhost:5173` no navegador.

Mantenha o backend e o frontend em execução simultaneamente.

## Integração

O frontend utiliza `fetch` envia requisições para `/api/checkin`.

Durante a execução com `npm run dev`, o proxy configurado no Vite encaminha essas requisições para `http://localhost:8080/checkin`.

Essa configuração corresponde ao ambiente local de desenvolvimento.

## Banco de dados

O projeto utiliza H2 em memória. Os registros são mantidos durante a execução e são perdidos quando o backend é encerrado.

A configuração está em:

`back-end/src/main/resources/application.properties`

A criação da tabela está em:

`back-end/src/main/resources/schema.sql`

Para consultar o banco pelo navegador, com o backend em execução:

- Endereço: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:meu_banco`
- Usuário: `sa`
- Senha: deixar vazia

## Regras de negócio

- É permitido apenas um check-in por dia.
- Não é permitido registrar datas e horas futuras.
- Quando a data e hora não são informadas, o backend presume que você quer usar sua data  horário atual.
- O tipo de treino é obrigatório e deve ter até 50 caracteres.
- A duração deve ser um número inteiro entre 1 e 1440 minutos.
- A intensidade deve ser `Leve`, `Moderada` ou `Intensa`.
- O local do treino é obrigatório e deve ter até 50 caracteres.

## Contrato da API

URL base: `http://localhost:8080`

### Consultar check-ins

**GET `/checkin`**

Não recebe parâmetros nem corpo de requisição.

Retorna os check-ins ordenados do mais recente para o mais antigo.

**Resposta: `200 OK`**

```json
[
  {
    "id": 1,
    "dataHora": "2026-09-08T18:30:00",
    "tipoTreino": "Peito e tríceps",
    "duracaoMinutos": 60,
    "intensidade": "Moderada",
    "localTreino": "Academia"
  }
]
```

Quando não existem registros, retorna `200 OK` com uma lista vazia:

```json
[]
```

### Cadastrar check-in

**POST `/checkin`**

Cabeçalho:

```text
Content-Type: application/json
```

| Campo | Tipo | Obrigatório | Regra |
| dataHora | String de data/hora | Não | Formato ISO local; não pode ser futura |
| tipoTreino | String | Sim | Não vazio, até 50 caracteres |
| duracaoMinutos | Integer | Sim | Entre 1 e 1440 |
| intensidade | String | Sim | Leve, Moderada ou Intensa |
| localTreino | String | Sim | Não vazio, até 50 caracteres |

O ID é gerado pelo banco e não precisa ser enviado.

**Exemplo de requisição:**

```json
{
  "dataHora": "2026-09-08T18:30:00",
  "tipoTreino": "Peito e tríceps",
  "duracaoMinutos": 60,
  "intensidade": "Moderada",
  "localTreino": "Academia"
}
```

Para utilizar o horário atual, omita `dataHora` ou envie `null`.

**Resposta: `201 Created`**

```json
{
  "id": 1,
  "dataHora": "2026-09-08T18:30:00",
  "tipoTreino": "Peito e tríceps",
  "duracaoMinutos": 60,
  "intensidade": "Moderada",
  "localTreino": "Academia"
}
```

**Exemplo de resposta: `400 Bad Request`**

```json
{
  "mensagem": "Não é permitido check-in no futuro."
}
```

**Exemplo de resposta: `409 Conflict`**

```json
{
  "mensagem": "Já existe um check-in nesse dia."
}
```

### Códigos HTTP

| 200 | Consulta realizada com sucesso |
| 201 | Check-in cadastrado com sucesso |
| 400 | Dados inválidos |
| 409 | Já existe um check-in no dia informado |

## Autor

Gustavo Kenzo Iwahashi

Tema: Academia
