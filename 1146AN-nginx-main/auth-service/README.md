# Auth Service - Sistema de Compra de Ingressos

Serviço de autenticação do sistema de compra de ingressos seguindo arquitetura hexagonal.

## Responsabilidades

- **Cadastro de usuários**: Registro de novos usuários
- **Autenticação**: Login e geração de tokens JWT
- **Gerenciamento de roles**: Controle de permissões (USER, ORGANIZER, ADMIN)
- **Recuperação de senha**: Magic links para reset de senha
- **Validação de tokens**: Verificação de JWT

## Arquitetura Hexagonal

```
auth-service/
└── src/main/java/com/example/authservice/
    ├── domain/                    # Camada de Domínio (Core)
    │   ├── auth/                 # Agregado de Autenticação
    │   │   ├── MagicLink.java
    │   │   ├── MagicLinkRepository.java (porta)
    │   │   └── vo/               # Value Objects
    │   └── user/                 # Agregado de Usuário
    │       ├── User.java
    │       ├── UserRepository.java (porta)
    │       └── vo/               # Value Objects
    │           ├── Email.java
    │           ├── Role.java
    │           └── RoleType.java  # USER, ORGANIZER, ADMIN
    │
    ├── application/               # Camada de Aplicação
    │   ├── auth/                 # Casos de uso de autenticação
    │   ├── user/                 # Casos de uso de usuário
    │   │   └── RegisterUserHandler.java
    │   └── port/                 # Portas da aplicação
    │       └── PasswordHasher.java
    │
    ├── infrastructure/            # Camada de Infraestrutura
    │   ├── persistence/          # Adaptadores de persistência (JPA)
    │   ├── security/             # Segurança (JWT, BCrypt)
    │   ├── mail/                 # Envio de e-mails
    │   └── config/               # Configurações
    │
    └── interfaces/                # Camada de Interface
        └── rest/                 # Controllers REST
            └── dto/              # DTOs de entrada/saída
```

## Roles do Sistema

### USER (nível 1)
- Usuário comum que compra ingressos
- Role padrão no cadastro
- Pode visualizar eventos e comprar ingressos

### ORGANIZER (nível 2)
- Organizador de eventos
- Herda permissões de USER
- Pode criar e gerenciar eventos
- Acessa dashboard de vendas

### ADMIN (nível 3)
- Administrador do sistema
- Herda permissões de ORGANIZER
- Acesso total ao sistema

## Funcionalidades

### Cadastro de Usuário
- Endpoint: `POST /api/auth/register`
- Validação de e-mail único
- Hash de senha com BCrypt
- Role padrão: USER

### Login
- Endpoint: `POST /api/auth/login`
- Validação de credenciais
- Geração de JWT com role do usuário

### Magic Link (Recuperação de Senha)
- Geração de token único
- Envio por e-mail
- Validação de expiração

## Princípios da Arquitetura Hexagonal

1. **Domínio no centro**: Regras de negócio isoladas
2. **Portas e Adaptadores**: Interfaces para comunicação
3. **Independência de frameworks**: Core não depende de Spring
4. **Testabilidade**: Fácil criar testes unitários do domínio
5. **Inversão de dependência**: Infraestrutura depende do domínio

## Tecnologias

- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- JWT (Auth0)
- BCrypt
- Lombok
