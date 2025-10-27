# Sistema de Compra de Ingressos

Sistema de compra de ingressos desenvolvido com Spring Boot e Arquitetura Hexagonal.

## Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **Spring Security**
- **Spring Mail**
- **H2 Database** (desenvolvimento)
- **PostgreSQL** (produção)
- **Lombok**
- **JWT** (autenticação)
- **Maven**

## Arquitetura

O projeto segue a **Arquitetura Hexagonal** (Ports and Adapters), organizada em módulos:

### Estrutura de Módulos

```
src/main/java/com/ticketsystem/
├── events/          # Módulo de Eventos
├── tickets/         # Módulo de Ingressos
├── users/           # Módulo de Usuários
├── organizers/      # Módulo de Organizadores
├── notifications/   # Módulo de Notificações
└── shared/          # Código compartilhado
```

### Camadas da Arquitetura Hexagonal

Cada módulo é organizado em três camadas:

#### 1. **Domain** (Núcleo)
- `model/`: Entidades e Value Objects
- `service/`: Lógica de negócio
- `port/`: Interfaces (Portas)

#### 2. **Application**
- `usecase/`: Casos de uso da aplicação

#### 3. **Infrastructure** (Adaptadores)
- `adapter/web/`: Controllers REST
- `adapter/persistence/`: Repositórios JPA
- `adapter/email/`: Serviços de e-mail

## Funcionalidades

### Visitantes (Sem autenticação)
- ✅ Visualizar eventos disponíveis
- ✅ Ver detalhes dos eventos
- ✅ Pesquisar eventos por nome

### Usuários (Com autenticação)
- ✅ Cadastro de conta
- ✅ Login
- ✅ Recuperação de senha
- ✅ Compra de ingressos
- ✅ Preenchimento de dados dos ingressos

### Organizadores
- ✅ Cadastro de eventos
- ✅ Dashboard com métricas
- ✅ Visualização de vendas

## Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Executar o projeto

```bash
./mvnw spring-boot:run
```

### Acessar o H2 Console

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:ticketdb
Username: sa
Password: (deixar em branco)
```

## Configuração

As configurações estão em `src/main/resources/application.properties`.

### Configurar e-mail

Para funcionalidade de recuperação de senha, configure:

```properties
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-app
```

## Desenvolvimento

### Estrutura do código

O projeto segue os princípios:
- **DDD** (Domain-Driven Design)
- **SOLID**
- **Clean Architecture**
- **Hexagonal Architecture**

### Padrões utilizados
- Repository Pattern
- Use Case Pattern
- Dependency Inversion

## Autor

Desenvolvido para o Sistema de Compra de Ingressos.
