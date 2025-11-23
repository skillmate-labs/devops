# Variáveis de configuração
RESOURCE_GROUP="rg-skillmate"
LOCATION="canadacentral"
ACR_NAME="skillmateacr"
ORACLE_CONTAINER="oracle-db"
RABBITMQ_CONTAINER="rabbitmq"
OLLAMA_CONTAINER="ollama"
API_CONTAINER="skillmate-api"
ORACLE_PASSWORD="SecurePassword123!"
RABBITMQ_USER="guest"
RABBITMQ_PASS="guest"
IMAGE_NAME="skillmate"
IMAGE_TAG="latest"

# ============================================
# 1. Login no Azure
# ============================================
az login

# ============================================
# 2. Criar Resource Group
# ============================================
az group create \
    --name $RESOURCE_GROUP \
    --location $LOCATION

# Verificar criação
az group show --name $RESOURCE_GROUP --output table

# ============================================
# 3. Criar Azure Container Registry
# ============================================
az acr create \
    --resource-group $RESOURCE_GROUP \
    --name $ACR_NAME \
    --sku Basic

# Habilitar admin user
az acr update --name $ACR_NAME --admin-enabled true

# Obter credenciais do ACR
ACR_USERNAME=$(az acr credential show --name $ACR_NAME --query username -o tsv)
ACR_PASSWORD=$(az acr credential show --name $ACR_NAME --query 'passwords[0].value' -o tsv)
ACR_LOGIN_SERVER=$(az acr show --name $ACR_NAME --query loginServer -o tsv)

echo "ACR Username: $ACR_USERNAME"
echo "ACR Login Server: $ACR_LOGIN_SERVER"

# ============================================
# 4. Build e Push da Imagem Docker
# ============================================
# Login no ACR
az acr login --name $ACR_NAME

# Navegar para o diretório raiz do projeto (onde está o Dockerfile)
cd "$(dirname "$0")/.."

# Build da imagem
docker build -t $ACR_LOGIN_SERVER/$IMAGE_NAME:$IMAGE_TAG .

# Push para o registry
docker push $ACR_LOGIN_SERVER/$IMAGE_NAME:$IMAGE_TAG

# Verificar imagem no registry
az acr repository list --name $ACR_NAME --output table

# ============================================
# 5. Deploy do Oracle Database
# ============================================
# NOTA: Para usar a imagem oficial do Oracle, você precisa:
# 1. Criar conta em https://container-registry.oracle.com
# 2. Aceitar termos de uso
# 3. Fazer login: docker login container-registry.oracle.com
# 4. Fornecer credenciais abaixo

# Alternativa: usar imagem pública (gvenzl/oracle-xe)
az container create \
    --resource-group $RESOURCE_GROUP \
    --name $ORACLE_CONTAINER \
    --image gvenzl/oracle-xe:21.3.0-slim \
    --location $LOCATION \
    --os-type Linux \
    --cpu 2 \
    --memory 8 \
    --ports 1521 \
    --ip-address Public \
    --environment-variables \
        ORACLE_PASSWORD=$ORACLE_PASSWORD

# Aguardar inicialização do Oracle (5-10 minutos)
echo "Aguardando inicialização do Oracle..."
sleep 60

# Monitorar logs até o Oracle estar pronto
echo "Monitorando logs do Oracle..."
timeout=600  # 10 minutos
elapsed=0
while [ $elapsed -lt $timeout ]; do
    if az container logs --resource-group $RESOURCE_GROUP --name $ORACLE_CONTAINER 2>/dev/null | grep -q "DATABASE IS READY TO USE\|Database ready to use"; then
        echo "Oracle Database está pronto!"
        break
    fi
    echo "Aguardando... ($elapsed/$timeout segundos)"
    sleep 30
    elapsed=$((elapsed + 30))
done

# Obter IP público do Oracle
ORACLE_IP=$(az container show \
    --name $ORACLE_CONTAINER \
    --resource-group $RESOURCE_GROUP \
    --query ipAddress.ip \
    -o tsv)

echo "Oracle IP: $ORACLE_IP"
echo "Oracle Port: 1521"

# ============================================
# 6. Deploy do RabbitMQ
# ============================================
az container create \
    --resource-group $RESOURCE_GROUP \
    --name $RABBITMQ_CONTAINER \
    --image rabbitmq:3-management \
    --location $LOCATION \
    --os-type Linux \
    --cpu 1 \
    --memory 1.5 \
    --ports 5672 15672 \
    --ip-address Public \
    --environment-variables \
        RABBITMQ_DEFAULT_USER=$RABBITMQ_USER \
        RABBITMQ_DEFAULT_PASS=$RABBITMQ_PASS

# Aguardar o container estar pronto
sleep 10

# Obter IP do RabbitMQ
RABBITMQ_IP=$(az container show \
    --name $RABBITMQ_CONTAINER \
    --resource-group $RESOURCE_GROUP \
    --query ipAddress.ip \
    -o tsv)

echo "RabbitMQ IP: $RABBITMQ_IP"
echo "RabbitMQ Management UI: http://$RABBITMQ_IP:15672"
echo "Username: $RABBITMQ_USER"
echo "Password: $RABBITMQ_PASS"

# ============================================
# 7. Deploy do Ollama
# ============================================
az container create \
    --resource-group $RESOURCE_GROUP \
    --name $OLLAMA_CONTAINER \
    --image ollama/ollama:latest \
    --location $LOCATION \
    --os-type Linux \
    --cpu 2 \
    --memory 4 \
    --ports 11434 \
    --ip-address Public \
    --environment-variables \
        OLLAMA_HOST=0.0.0.0

# Aguardar o container estar pronto
sleep 10

# Obter IP do Ollama
OLLAMA_IP=$(az container show \
    --name $OLLAMA_CONTAINER \
    --resource-group $RESOURCE_GROUP \
    --query ipAddress.ip \
    -o tsv)

echo "Ollama IP: $OLLAMA_IP"
echo "Ollama Port: 11434"

# Aguardar inicialização do Ollama
echo "Aguardando inicialização do Ollama..."
sleep 20

# ============================================
# 8. Deploy da API
# ============================================
# Construir connection string do Oracle
ORACLE_CONNECTION_STRING="jdbc:oracle:thin:@$ORACLE_IP:1521:XE"

# Criar container da API
az container create \
    --resource-group $RESOURCE_GROUP \
    --name $API_CONTAINER \
    --image $ACR_LOGIN_SERVER/$IMAGE_NAME:$IMAGE_TAG \
    --location $LOCATION \
    --os-type Linux \
    --cpu 2 \
    --memory 2 \
    --ports 8080 \
    --ip-address Public \
    --registry-login-server $ACR_LOGIN_SERVER \
    --registry-username $ACR_USERNAME \
    --registry-password $ACR_PASSWORD \
    --environment-variables \
        SPRING_DATASOURCE_URL=$ORACLE_CONNECTION_STRING \
        SPRING_DATASOURCE_USERNAME=system \
        SPRING_DATASOURCE_PASSWORD=$ORACLE_PASSWORD \
        SPRING_DATASOURCE_DRIVERCLASSNAME=oracle.jdbc.OracleDriver \
        SPRING_RABBITMQ_HOST=$RABBITMQ_IP \
        SPRING_RABBITMQ_PORT=5672 \
        SPRING_RABBITMQ_USERNAME=$RABBITMQ_USER \
        SPRING_RABBITMQ_PASSWORD=$RABBITMQ_PASS \
        SPRING_AI_OLLAMA_BASE_URL=http://$OLLAMA_IP:11434 \
        SPRING_AI_OLLAMA_CHAT_OPTIONS_MODEL=llama3.2:3b \
        SPRING_AI_OLLAMA_CHAT_OPTIONS_TEMPERATURE=0.7 \
        JWT_SECRET=mySecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLongForSecurity \
        JWT_EXPIRATION=86400000 \
        SERVER_PORT=8080

# Aguardar o container estar pronto
sleep 15

# Obter IP público da API
API_IP=$(az container show \
    --name $API_CONTAINER \
    --resource-group $RESOURCE_GROUP \
    --query ipAddress.ip \
    -o tsv)

echo "API IP: $API_IP"
echo "API URL: http://$API_IP:8080"

# ============================================
# 9. Comandos de Conexão com o Banco de Dados
# ============================================
# Conectar via SQL*Plus (dentro do container)
# az container exec --resource-group $RESOURCE_GROUP --name $ORACLE_CONTAINER --exec-command "sqlplus system/$ORACLE_PASSWORD@XE"

# Conectar via SQL*Plus (remoto)
# sqlplus system/$ORACLE_PASSWORD@$ORACLE_IP:1521/XE

# Comandos de formatação no SQL*Plus (executar após conectar):
# SET LINESIZE 200
# SET PAGESIZE 50
