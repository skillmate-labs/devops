# Skillmate API

## 🚀 Sobre o Projeto

A **Skillmate API** é uma aplicação Spring Boot que expõe APIs RESTful para gestão de usuários, papéis e metas (goals) de aprendizado. O projeto utiliza Oracle Database, autenticação via JWT, cache, paginação, mensageria com RabbitMQ e integração com IA (Ollama) para sugestões inteligentes de metas de aprendizado.

## 🎥 Vídeo Demonstrativo

Assista ao vídeo demonstrativo da solução: [SkillMate - Demonstração](https://youtu.be/pITGGdg0LCQ)

## 👥 Equipe de Desenvolvimento

| Nome                        | RM      | Turma    | E-mail                 | GitHub                                         | LinkedIn                                   |
|-----------------------------|---------|----------|------------------------|------------------------------------------------|--------------------------------------------|
| Arthur Vieira Mariano       | RM554742| 2TDSPF   | arthvm@proton.me       | [@arthvm](https://github.com/arthvm)           | [arthvm](https://linkedin.com/in/arthvm/)  |
| Guilherme Henrique Maggiorini| RM554745| 2TDSPF  | guimaggiorini@gmail.com| [@guimaggiorini](https://github.com/guimaggiorini) | [guimaggiorini](https://linkedin.com/in/guimaggiorini/) |
| Ian Rossato Braga           | RM554989| 2TDSPY   | ian007953@gmail.com    | [@iannrb](https://github.com/iannrb)           | [ianrossato](https://linkedin.com/in/ianrossato/)      |

## 🛠️ Tecnologias Utilizadas

- **Java 17**, **Spring Boot 3.5.8**
- **Spring Web**, **Spring Data JPA** (Oracle)
- **Spring Security** com **JWT** (jjwt 0.12.3)
- **Bean Validation (Jakarta)**
- **Spring Cache** (Caffeine) e paginação do Spring Data
- **RabbitMQ** para mensageria assíncrona
- **Spring AI** com **Ollama** para sugestões de metas via IA
- **Spring Actuator** para monitoramento
- **Lombok** para redução de boilerplate
- **Apache Commons Lang3** para utilitários
- **spring-dotenv 4.0.0** (variáveis de ambiente)
- **Oracle JDBC Driver 19.8.0.0**
- **BCrypt** para hash de senhas
- **Internacionalização (i18n)** com suporte a múltiplos idiomas

## 📦 Estrutura do Projeto

- `com/skillmate/skillmate/modules/*`: domínios (`auth`, `users`, `roles`, `goals`)
  - `controllers`: APIs REST sob `/api/*`
  - `useCases`: casos de uso da aplicação
  - `dto`, `mapper`, `entities`: camadas de dados
  - `repositories`: interfaces Spring Data JPA
- `config`: `SecurityConfig`, `RabbitMQConfig`, `CacheConfig`, `WebMvcConfig`
- `security`: `JwtTokenProvider`, `JwtAuthenticationFilter`
- `exception`: tratamento global de exceções
- `resources/messages*.properties`: arquivos de internacionalização

## 🔐 Segurança e Autenticação

### APIs REST (`/api/**`)
- Protegidas por **JWT Bearer Token**
- **Endpoints públicos:**
  - `/api/auth/**` (login)
  - `/api/users/register` (registro de usuários)
  - `/actuator/**` (monitoramento)
- **Endpoints protegidos:**
  - `/api/roles/**` → Requer `ROLE_ADM` (todos os endpoints)
  - `/api/goals/**` → Requer autenticação
  - `/api/users/**` → Requer autenticação (exceto `/register`)
- **Header obrigatório:** `Authorization: Bearer <token>`
- **Política de sessão:** STATELESS (não mantém sessão)

## 📜 Documentação e Monitoramento

### Spring Actuator
- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`
- Metrics: `http://localhost:8080/actuator/metrics`

## 🗄️ Banco de Dados

- **Banco:** Oracle Database (dialeto `org.hibernate.dialect.OracleDialect`)
- **DDL:** Desabilitado automaticamente (`spring.jpa.hibernate.ddl-auto=none`)
- **Criação de tabelas:** Deve ser feita manualmente ou via scripts SQL

### Entidades Principais
- **UserEntity** — Usuários do sistema com autenticação JWT
- **RoleEntity** — Papéis/permissões (USER, ADM)
- **GoalEntity** — Metas de aprendizado associadas a usuários

## ⚙️ Configuração e Execução

### Pré-requisitos
- Java 17
- Maven 3.6+
- Docker e Docker Compose
- Oracle Database (ou acesso a um)

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```bash
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver
```

### 🐳 Iniciar Serviços com Docker Compose

O projeto inclui um `compose.yaml` para RabbitMQ e Ollama:

```bash
# Inicia os serviços
docker compose up -d

# Para parar os serviços
docker compose down
```

**Serviços disponíveis:**
- **RabbitMQ Management UI:** `http://localhost:15672` (guest/guest)
- **RabbitMQ AMQP:** `localhost:5672`
- **Ollama API:** `http://localhost:11434`

### Configurar Modelo Ollama

Após iniciar o Ollama, baixe o modelo necessário:

1. **Aguarde alguns segundos** para o Ollama iniciar completamente
2. **Baixe o modelo:**
   ```bash
   docker compose exec ollama ollama pull llama3.2:3b
   ```
3. **Verifique o download:**
   ```bash
   docker compose exec ollama ollama list
   ```

**Nota:** O download pode levar alguns minutos. O modelo é necessário para as sugestões de IA funcionarem.

### 🚀 Executar a Aplicação

1. **Clone o repositório:**
   ```bash
   git clone <seu-repositorio>
   cd java
   ```

2. **Configure o `.env`** (veja seção anterior)

3. **Inicie os serviços** (RabbitMQ e Ollama) com Docker Compose (veja seção anterior)

4. **Compile e execute:**
   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`

**Configurações adicionais:**
- JWT Secret: configurado em `application.properties` (use variável de ambiente em produção)
- Todas as configurações estão em `src/main/resources/application.properties`

## 🔑 Fluxo de Autenticação

### Para APIs REST
1. **Criar roles** (se necessário): `POST /api/roles` (requer `ROLE_ADM`)
2. **Registrar usuário:** `POST /api/users/register`
3. **Fazer login:** `POST /api/auth/login` → retorna JWT
4. **Usar token:** Incluir `Authorization: Bearer <token>` nos headers das requisições protegidas

### Exemplo de Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": "user_id_aqui",
  "email": "usuario@example.com",
  "role": "USER"
}
```

## 📋 Endpoints Principais

### 🔑 Autenticação (`/api/auth`)
- `POST /api/auth/login` — autentica e retorna JWT

### 👥 Usuários (`/api/users`)
- `POST /api/users/register` — cria usuário (público)
- `GET /api/users` — lista paginada (autenticado)
- `GET /api/users/{id}` — obter por ID (autenticado)
- `PUT /api/users/{id}` — atualizar (autenticado)
- `DELETE /api/users/{id}` — excluir (requer `ROLE_ADM`)

### 🎭 Papéis (`/api/roles`) [requer `ROLE_ADM`]
- `GET /api/roles` — lista todos os papéis
- `GET /api/roles/paginated` — lista paginada
- `GET /api/roles/{id}` — obter por ID
- `POST /api/roles` — criar papel
- `PUT /api/roles/{id}` — atualizar
- `DELETE /api/roles/{id}` — excluir

### 🎯 Metas (`/api/goals`) [requer autenticação]
- `GET /api/goals` — lista paginada (pode filtrar por `userId`)
- `GET /api/goals/{id}` — obter por ID
- `POST /api/goals` — criar meta (associada ao usuário autenticado)
- `PUT /api/goals/{id}` — atualizar meta
- `DELETE /api/goals/{id}` — excluir meta
- `POST /api/goals/ai-suggestion` — obter sugestão de meta via IA

### 🤖 Sugestão de Meta via IA (`/api/goals/ai-suggestion`)
Gera sugestões inteligentes baseadas em experiência e habilidade desejada usando Ollama.

**Exemplo:**
```bash
curl -X POST http://localhost:8080/api/goals/ai-suggestion \
  -H "Content-Type: application/json" \
  -d '{
    "experience": "Tenho experiência básica em Java",
    "skill": "Spring Boot"
  }'
```

**Nota:** Requer o modelo Ollama `llama3.2:3b` configurado (veja seção "Configurar Modelo Ollama").

## 🧪 Exemplos de Testes

Esta seção contém exemplos práticos de requisições HTTP utilizadas para testar a API. Os exemplos abaixo foram executados contra um ambiente de produção em `http://4.239.133.229:8080`.

### 👥 Testes de Usuários

#### Criar Usuário (POST)
```http
POST http://4.239.133.229:8080/api/users/register
Content-Type: application/json

{
  "name": "Ian Devops",
  "email": "ian.devops@example.com",
  "password": "senha123",
  "roleId": "ysyrbingirour0g41khpcgd8"
}
```

#### Listar Usuários (GET)
```http
GET http://4.239.133.229:8080/api/users?size=100
Authorization: Bearer <token>
```

#### Atualizar Usuário (PUT)
```http
PUT http://4.239.133.229:8080/api/users/7da1e6d2652549cb91eb3ce5
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Ian Devops Edit",
  "email": "ian.devops@example.com",
  "password": "senha123",
  "roleId": "ysyrbingirour0g41khpcgd8"
}
```

#### Excluir Usuário (DELETE)
```http
DELETE http://4.239.133.229:8080/api/users/7da1e6d2652549cb91eb3ce5
Authorization: Bearer <token>
```

### 🔑 Testes de Autenticação

#### Login (POST)
```http
POST http://4.239.133.229:8080/api/auth/login
Content-Type: application/json

{
  "email": "ian.devops@example.com",
  "password": "senha123"
}
```

### 🎭 Testes de Papéis (Roles)

#### Listar Papéis (GET)
```http
GET http://4.239.133.229:8080/api/roles
Authorization: Bearer <token>
```

#### Criar Papel (POST)
```http
POST http://4.239.133.229:8080/api/roles
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Devops Vídeo",
  "acronym": "DVPV"
}
```

#### Atualizar Papel (PUT)
```http
PUT http://4.239.133.229:8080/api/roles/569b3b228cd242d19513f3c3
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Devops Vídeo Edit",
  "acronym": "DVPV"
}
```

#### Excluir Papel (DELETE)
```http
DELETE http://4.239.133.229:8080/api/roles/569b3b228cd242d19513f3c3
Authorization: Bearer <token>
```

**Nota:** Para endpoints protegidos, substitua `<token>` pelo JWT retornado no login.

## 🌍 Internacionalização (i18n)

O projeto suporta múltiplos idiomas através dos arquivos de propriedades:
- `messages.properties` — Inglês (padrão)
- `messages_pt_BR.properties` — Português (Brasil)

As mensagens de validação e erros são traduzidas automaticamente baseadas no header `Accept-Language` da requisição.

## 🏗️ Arquitetura

### Padrões Utilizados
- **Clean Architecture** com separação por módulos
- **Use Cases** para lógica de negócio
- **DTOs** para transferência de dados
- **Mappers** para conversão entre entidades e DTOs
- **Repository Pattern** com Spring Data JPA

### Componentes Principais

**Mensageria (RabbitMQ):**
- Comunicação assíncrona com produtores e consumidores
- Suporte a filas e exchanges
- Processamento em background

**Cache (Caffeine):**
- Cache de usuários, metas e papéis
- Melhora performance de consultas frequentes
- Invalidação automática em operações de escrita

**IA (Ollama + Spring AI):**
- Integração com Ollama via Spring AI
- Modelo `llama3.2:3b` para sugestões de metas
- Sugestões personalizadas baseadas em experiência e habilidade

## 📄 Licença

Projeto acadêmico desenvolvido na Global Solution da FIAP.
