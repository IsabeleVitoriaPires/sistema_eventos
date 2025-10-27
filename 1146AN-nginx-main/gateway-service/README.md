# Gateway Service - Sistema de Compra de Ingressos

API Gateway do sistema de compra de ingressos seguindo arquitetura hexagonal.

## Responsabilidades

- **Roteamento**: Encaminhar requisições para os serviços corretos
- **Autenticação**: Validar tokens JWT
- **Autorização**: Controlar acesso baseado em roles (USER, ORGANIZER, ADMIN)
- **Segurança**: Ponto único de entrada para o sistema

## Arquitetura Hexagonal

```
gateway-service/
└── src/main/java/com/example/gateway_service/
    ├── domain/              # Camada de Domínio
    │   └── user/
    │       └── vo/
    │           └── RoleType.java  # Enum com roles do sistema
    │
    ├── application/         # Camada de Aplicação
    │   └── port/           # Portas (interfaces)
    │
    └── infrastructure/      # Camada de Infraestrutura
        ├── config/         # Configurações
        └── security/       # Segurança e filtros
            └── AuthorizationFilter.java
```

## Roles do Sistema

### USER (nível 1)
- Comprar ingressos
- Visualizar pedidos
- Acesso: `/api/tickets/purchase`, `/api/purchases`

### ORGANIZER (nível 2)
- Todas as permissões de USER
- Criar e gerenciar eventos
- Visualizar dashboard de vendas
- Acesso: `/api/events/create`, `/api/events/manage`, `/api/dashboard`

### ADMIN (nível 3)
- Todas as permissões de ORGANIZER
- Administração completa do sistema
- Acesso: `/api/admin`

## Rotas Protegidas

| Rota | Role Mínima | Descrição |
|------|-------------|-----------|
| `/api/tickets/purchase` | USER | Compra de ingressos |
| `/api/purchases` | USER | Histórico de compras |
| `/api/events/create` | ORGANIZER | Criar eventos |
| `/api/events/manage` | ORGANIZER | Gerenciar eventos |
| `/api/dashboard` | ORGANIZER | Dashboard do organizador |
| `/api/admin` | ADMIN | Área administrativa |

## Fluxo de Autorização

1. Cliente faz requisição com token JWT no header `Authorization: Bearer <token>`
2. AuthorizationFilter intercepta a requisição
3. Valida o token JWT
4. Extrai a role do usuário
5. Verifica se a role tem permissão para acessar a rota
6. Permite ou nega o acesso (401/403)

## Configuração

```properties
jwt.secret=sua-chave-secreta
```

## Tecnologias

- Spring Cloud Gateway
- JWT (Auth0)
- Spring WebFlux (Reativo)
