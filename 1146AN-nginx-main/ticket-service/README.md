# Ticket Service - Sistema de Compra de Ingressos

Serviço de gerenciamento de ingressos e eventos.

## Status

⚠️ **Serviço em desenvolvimento** - Atualmente contém apenas endpoint de health check.

## Endpoints

### Health Check
```
GET /api/tickets/_health
```

**Resposta:**
```json
{
  "service": "ticket-service",
  "status": "OK"
}
```

## Como Rodar

### Com Maven:
```bash
./mvnw spring-boot:run
```

### Com Docker:
```bash
docker build -t ticket-service .
docker run -p 8085:8080 ticket-service
```

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Maven

## Porta

- **8085** (quando rodando via docker-compose)
- **8080** (porta interna do container)
