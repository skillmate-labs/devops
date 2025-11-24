# Skillmate API

## 🚀 Sobre o Projeto

A **Skillmate API** é uma aplicação Spring Boot que expõe APIs RESTful para gestão de usuários, papéis e metas (goals) de aprendizado. O projeto utiliza Oracle Database, autenticação via JWT, cache, paginação, mensageria com RabbitMQ e integração com IA (Ollama) para sugestões inteligentes de metas de aprendizado.

O projeto inclui uma infraestrutura completa de DevOps com CI/CD através do Azure DevOps, containerização Docker, e scripts automatizados para deploy na nuvem Azure.

## 🎥 Vídeo Demonstrativo

Assista ao vídeo demonstrativo da solução: [SkillMate - Demonstração]()

## 👥 Equipe de Desenvolvimento

| Nome                        | RM      | Turma    | E-mail                 | GitHub                                         | LinkedIn                                   |
|-----------------------------|---------|----------|------------------------|------------------------------------------------|--------------------------------------------|
| Arthur Vieira Mariano       | RM554742| 2TDSPF   | arthvm@proton.me       | [@arthvm](https://github.com/arthvm)           | [arthvm](https://linkedin.com/in/arthvm/)  |
| Guilherme Henrique Maggiorini| RM554745| 2TDSPF  | guimaggiorini@gmail.com| [@guimaggiorini](https://github.com/guimaggiorini) | [guimaggiorini](https://linkedin.com/in/guimaggiorini/) |
| Ian Rossato Braga           | RM554989| 2TDSPY   | ian007953@gmail.com    | [@iannrb](https://github.com/iannrb)           | [ianrossato](https://linkedin.com/in/ianrossato/)      |

## 🛠️ Tecnologias Utilizadas

### Backend
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

### DevOps e Infraestrutura
- **Docker** e **Docker Compose** para containerização
- **Azure DevOps** para CI/CD
- **Azure Container Registry (ACR)** para armazenamento de imagens
- **Azure Container Instances** para deploy de containers
- **Maven 3.9.6** para gerenciamento de dependências e build
- **Azure CLI** para automação de infraestrutura

## 📦 Estrutura do Projeto

### Código da Aplicação
- `com/skillmate/skillmate/modules/*`: domínios (`auth`, `users`, `roles`, `goals`)
  - `controllers`: APIs REST sob `/api/*`
  - `useCases`: casos de uso da aplicação
  - `dto`, `mapper`, `entities`: camadas de dados
  - `repositories`: interfaces Spring Data JPA
- `config`: `SecurityConfig`, `RabbitMQConfig`, `CacheConfig`, `WebMvcConfig`
- `security`: `JwtTokenProvider`, `JwtAuthenticationFilter`
- `exception`: tratamento global de exceções
- `resources/messages*.properties`: arquivos de internacionalização

### Arquivos DevOps
- `Dockerfile`: Imagem Docker multi-stage para produção
- `compose.yaml`: Docker Compose para desenvolvimento local (RabbitMQ, Ollama)
- `azure-pipeline.yml`: Pipeline CI/CD do Azure DevOps
- `scripts/script-infra.sh`: Script automatizado de deploy na Azure
- `scripts/script-bd.sql`: Script SQL para criação das tabelas
- `pom.xml`: Configuração Maven com todas as dependências
- `mvnw` / `mvnw.cmd`: Maven Wrapper para builds sem Maven instalado

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
- Maven 3.6+ (ou use o `mvnw` incluído no projeto)
- Docker e Docker Compose
- Oracle Database (ou acesso a um)
- Azure CLI (para deploy na nuvem - opcional)

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto para desenvolvimento local:

```bash
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver
```

**Nota:** As variáveis de ambiente também podem ser configuradas diretamente no sistema ou através do Azure Container Instances durante o deploy.

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

#### Desenvolvimento Local

1. **Clone o repositório:**
   ```bash
   git clone <seu-repositorio>
   cd devops
   ```

2. **Configure o `.env`** (veja seção anterior)

3. **Inicie os serviços** (RabbitMQ e Ollama) com Docker Compose (veja seção anterior)

4. **Execute o script SQL** para criar as tabelas no banco de dados:
   ```bash
   # Conecte-se ao Oracle e execute:
   sqlplus <usuario>/<senha>@<host>:<port>/<sid> @scripts/script-bd.sql
   ```

5. **Compile e execute:**
   ```bash
   # Usando Maven Wrapper (recomendado)
   ./mvnw clean compile
   ./mvnw spring-boot:run
   
   # Ou usando Maven instalado
   mvn clean compile
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`

**Configurações adicionais:**
- JWT Secret: configurado em `application.properties` (use variável de ambiente em produção)
- Todas as configurações estão em `src/main/resources/application.properties`

#### Executar com Docker

1. **Build da imagem:**
   ```bash
   docker build -t skillmate:latest .
   ```

2. **Execute o container:**
   ```bash
   docker run -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@<host>:<port>:<sid> \
     -e SPRING_DATASOURCE_USERNAME=<username> \
     -e SPRING_DATASOURCE_PASSWORD=<password> \
     -e SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver \
     -e SPRING_RABBITMQ_HOST=<rabbitmq-host> \
     -e SPRING_AI_OLLAMA_BASE_URL=http://<ollama-host>:11434 \
     skillmate:latest
   ```

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

## 🔄 CI/CD e DevOps

### Azure DevOps Pipeline

O projeto inclui um pipeline de CI/CD configurado no Azure DevOps (`azure-pipeline.yml`) que:

- **Trigger:** Executa automaticamente em commits nas branches `main` e `dev`
- **Build:** Compila o projeto Maven com Java 17
- **Cache:** Utiliza cache de dependências Maven para otimizar builds
- **Artefatos:** Gera e publica o JAR da aplicação como artefato

**Configuração do Pipeline:**
- **VM Image:** `ubuntu-latest`
- **Java Version:** 17
- **Maven Version:** 3.9.6
- **Artefato:** `skillmate-jar` (contém o JAR compilado)

Para configurar o pipeline no Azure DevOps:
1. Conecte seu repositório ao Azure DevOps
2. Crie um novo pipeline e selecione o arquivo `azure-pipeline.yml`
3. O pipeline será executado automaticamente em cada push

### Deploy na Azure

O projeto inclui um script automatizado (`scripts/script-infra.sh`) para deploy completo da infraestrutura na Azure:

#### Recursos Criados:
- **Resource Group:** `rg-skillmate` (região: Canada Central)
- **Azure Container Registry (ACR):** Para armazenar imagens Docker
- **Azure Container Instances:**
  - Oracle Database (gvenzl/oracle-xe)
  - RabbitMQ (com Management UI)
  - Ollama (serviço de IA)
  - Skillmate API (aplicação principal)

#### Executar Deploy:

1. **Instale o Azure CLI:**
   ```bash
   # macOS
   brew install azure-cli
   
   # Ou baixe de: https://aka.ms/installazurecliwindows
   ```

2. **Execute o script de infraestrutura:**
   ```bash
   cd scripts
   chmod +x script-infra.sh
   ./script-infra.sh
   ```

3. **O script irá:**
   - Fazer login no Azure
   - Criar o Resource Group
   - Criar o Azure Container Registry
   - Build e push da imagem Docker
   - Deploy de todos os containers
   - Configurar variáveis de ambiente automaticamente
   - Exibir os IPs públicos de cada serviço

**Tempo estimado:** 15-20 minutos (incluindo inicialização do Oracle)

#### Variáveis de Ambiente no Deploy

O script configura automaticamente todas as variáveis necessárias:
- Conexão com Oracle Database
- Configuração do RabbitMQ
- URL do Ollama
- JWT Secret e expiração
- Portas e hosts dos serviços

#### Acessar Serviços Após Deploy

Após o deploy, o script exibirá os IPs públicos. Acesse:
- **API:** `http://<API_IP>:8080`
- **RabbitMQ Management:** `http://<RABBITMQ_IP>:15672` (guest/guest)
- **Ollama:** `http://<OLLAMA_IP>:11434`
- **Oracle:** `<ORACLE_IP>:1521`

### Docker

#### Dockerfile

O projeto inclui um `Dockerfile` multi-stage que:
- **Stage 1 (Build):** Usa `maven:3.9.6-eclipse-temurin-17` para compilar
- **Stage 2 (Runtime):** Usa `eclipse-temurin:17-jre` para executar
- **Otimizações:** Configurações de memória para containers
- **Porta:** Expõe a porta 8080

#### Docker Compose (Desenvolvimento)

O `compose.yaml` inclui serviços para desenvolvimento local:
- **RabbitMQ:** Com interface de gerenciamento
- **Ollama:** Para sugestões de IA

**Uso:**
```bash
# Iniciar serviços
docker compose up -d

# Parar serviços
docker compose down

# Ver logs
docker compose logs -f
```

### Scripts de Banco de Dados

O arquivo `scripts/script-bd.sql` contém o DDL completo para criação das tabelas:
- `roles` - Papéis de usuários
- `users` - Usuários do sistema
- `goals` - Metas de aprendizado
- `weekly_plans` - Planos semanais
- `tasks` - Tarefas dos planos
- `references` - Referências de aprendizado

**Executar:**
```bash
sqlplus <usuario>/<senha>@<host>:<port>/<sid> @scripts/script-bd.sql
```

## 📊 Monitoramento e Observabilidade

### Health Checks

A aplicação expõe endpoints do Spring Actuator para monitoramento:
- **Health:** `http://localhost:8080/actuator/health`
- **Info:** `http://localhost:8080/actuator/info`
- **Metrics:** `http://localhost:8080/actuator/metrics`

### Logs

No Azure Container Instances, visualize os logs:
```bash
# Logs da API
az container logs --resource-group rg-skillmate --name skillmate-api --follow

# Logs do Oracle
az container logs --resource-group rg-skillmate --name oracle-db --follow

# Logs do RabbitMQ
az container logs --resource-group rg-skillmate --name rabbitmq --follow

# Logs do Ollama
az container logs --resource-group rg-skillmate --name ollama --follow
```

## 🔧 Troubleshooting

### Problemas Comuns

1. **Oracle não inicia:**
   - Aguarde 5-10 minutos após o deploy
   - Verifique os logs: `az container logs --resource-group rg-skillmate --name oracle-db`

2. **API não conecta ao banco:**
   - Verifique se o Oracle está pronto
   - Confirme o IP e porta do Oracle
   - Verifique as variáveis de ambiente no container da API

3. **Ollama não responde:**
   - Aguarde alguns minutos após o deploy
   - Verifique se o modelo foi baixado: `docker compose exec ollama ollama list`
   - Baixe o modelo manualmente se necessário

4. **Build do pipeline falha:**
   - Verifique se o Java 17 está configurado corretamente
   - Confirme que o `pom.xml` está válido
   - Verifique os logs do pipeline no Azure DevOps

## 📄 Licença

Projeto acadêmico desenvolvido na Global Solution da FIAP.
