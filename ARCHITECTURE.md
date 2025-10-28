# Arquitetura Hexagonal

Este documento explica a organização da Arquitetura Hexagonal no projeto.

## O que é Arquitetura Hexagonal?

Também conhecida como **Ports and Adapters**, a Arquitetura Hexagonal foi criada por Alistair Cockburn. O objetivo é criar aplicações que sejam:
- **Independentes de frameworks**
- **Testáveis**
- **Independentes da UI**
- **Independentes de banco de dados**
- **Independentes de agentes externos**

## Estrutura do Projeto

### Camadas

```
┌─────────────────────────────────────────┐
│         Infrastructure Layer            │
│  (Adapters: Web, Persistence, Email)    │
│  - Controllers REST                     │
│  - JPA Repositories                     │
│  - Email Senders                        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Application Layer               │
│  (Use Cases)                            │
│  - Business orchestration               │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Domain Layer                  │
│  (Business Logic - Core)                │
│  - Entities                             │
│  - Value Objects                        │
│  - Domain Services                      │
│  - Ports (Interfaces)                   │
└─────────────────────────────────────────┘
```

### Fluxo de Dependências

```
Infrastructure → Application → Domain
```

**Regra de Ouro**: Dependências sempre apontam para dentro (em direção ao Domain).

## Organização por Módulo

Cada módulo (events, tickets, users, etc.) tem a mesma estrutura:

```
module/
├── domain/
│   ├── model/          # Entidades e Value Objects
│   ├── service/        # Serviços de domínio
│   └── port/           # Interfaces (Portas)
│       ├── in/         # Portas de entrada (Use Cases)
│       └── out/        # Portas de saída (Repositories, etc)
│
├── application/
│   └── usecase/        # Implementação dos Use Cases
│
└── infrastructure/
    └── adapter/
        ├── web/        # Controllers REST (Entrada)
        └── persistence/ # Repositories JPA (Saída)
```

## Exemplo: Módulo de Eventos

### Domain Layer

**Entidade**: `Event.java`
```java
package com.ticketsystem.events.domain.model;

public class Event {
    private Long id;
    private String name;
    private String description;
    // ...
}
```

**Porta de Entrada**: `CreateEventUseCase.java`
```java
package com.ticketsystem.events.domain.port.in;

public interface CreateEventUseCase {
    Event create(EventDTO dto);
}
```

**Porta de Saída**: `EventRepository.java`
```java
package com.ticketsystem.events.domain.port.out;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(Long id);
}
```

### Application Layer

**Use Case**: `CreateEventUseCaseImpl.java`
```java
package com.ticketsystem.events.application.usecase;

@Service
public class CreateEventUseCaseImpl implements CreateEventUseCase {
    private final EventRepository repository;

    public Event create(EventDTO dto) {
        // lógica de negócio
        return repository.save(event);
    }
}
```

### Infrastructure Layer

**Controller**: `EventController.java`
```java
package com.ticketsystem.events.infrastructure.adapter.web;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final CreateEventUseCase createEventUseCase;

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody EventDTO dto) {
        return ResponseEntity.ok(createEventUseCase.create(dto));
    }
}
```

**Repository**: `EventJpaRepository.java`
```java
package com.ticketsystem.events.infrastructure.adapter.persistence;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long>, EventRepository {
    // Implementação automática pelo Spring Data JPA
}
```

## Benefícios

1. **Testabilidade**: Pode testar o domínio sem infraestrutura
2. **Flexibilidade**: Fácil trocar adaptadores (ex: mudar de H2 para PostgreSQL)
3. **Manutenibilidade**: Código organizado e com responsabilidades claras
4. **Independência**: Core da aplicação não depende de frameworks

## Princípios SOLID Aplicados

- **S**ingle Responsibility: Cada classe tem uma única responsabilidade
- **O**pen/Closed: Aberto para extensão, fechado para modificação
- **L**iskov Substitution: Subtipos devem ser substituíveis
- **I**nterface Segregation: Interfaces específicas e coesas
- **D**ependency Inversion: Dependa de abstrações, não de implementações

## Referências

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
