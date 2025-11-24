# Frontend - Sistema de Eventos

Frontend simples em HTML/CSS/JavaScript puro para o Sistema de Eventos.

## Páginas Disponíveis

1. **index.html** - Página principal com lista de eventos (pública)
2. **login.html** - Login e cadastro de usuários
3. **evento-detalhes.html** - Detalhes de um evento específico
4. **comprar.html** - Formulário de compra de ingressos (requer login)
5. **criar-evento.html** - Criar novo evento (apenas ORGANIZER)
6. **dashboard.html** - Dashboard com métricas (apenas ORGANIZER)

## Como Executar

### Opção 1: Abrir Diretamente no Navegador

1. Certifique-se de que os serviços backend estão rodando:
   - auth-service (porta 8084)
   - event-service (porta 8083)
   - ticket-service (porta 8085)

2. Abra o arquivo `index.html` diretamente no navegador:
   ```
   C:\Users\isabe\sistema_eventos\1146AN-nginx-main\frontend\index.html
   ```

### Opção 2: Usar um Servidor HTTP Local

Para evitar problemas de CORS:

**Usando Python:**
```bash
cd C:\Users\isabe\sistema_eventos\1146AN-nginx-main\frontend
python -m http.server 3000
```

Acesse: http://localhost:3000

**Usando Node.js (http-server):**
```bash
npm install -g http-server
cd C:\Users\isabe\sistema_eventos\1146AN-nginx-main\frontend
http-server -p 3000
```

Acesse: http://localhost:3000

**Usando Live Server (VS Code):**
1. Instale a extensão "Live Server" no VS Code
2. Clique com botão direito em `index.html`
3. Selecione "Open with Live Server"

## Fluxo de Uso

### Para Usuários (Comprar Ingressos)

1. **Navegar pelos eventos**
   - Abra `index.html`
   - Veja a lista de eventos disponíveis
   - Use a busca para filtrar eventos

2. **Cadastrar conta**
   - Clique em "Login" → "Cadastre-se"
   - Preencha: nome, sobrenome, email, senha
   - Escolha "Usuário (Comprar Ingressos)"
   - Clique em "Cadastrar"

3. **Fazer login**
   - Use email e senha criados
   - Clique em "Entrar"

4. **Comprar ingressos**
   - Clique em um evento → "Ver Detalhes"
   - Clique em "Comprar Ingressos"
   - Escolha quantidade (máximo 10)
   - Preencha dados de CADA participante:
     - Nome completo
     - CPF (11 dígitos sem formatação)
     - Email
     - Data de nascimento
   - Clique em "Finalizar Compra"

### Para Organizadores (Criar Eventos)

1. **Cadastrar como organizador**
   - Clique em "Login" → "Cadastre-se"
   - Preencha dados pessoais
   - Escolha "Organizador (Criar Eventos)"
   - Clique em "Cadastrar"

2. **Fazer login**
   - Use email e senha

3. **Criar evento**
   - Clique em "Criar Evento" no menu
   - Preencha:
     - Nome do evento
     - Descrição
     - Data e hora
     - Local
     - Preço do ingresso
     - Quantidade de ingressos
   - Clique em "Criar Evento"

4. **Ver dashboard**
   - Clique em "Dashboard" no menu
   - Veja estatísticas gerais:
     - Total de eventos
     - Ingressos vendidos
     - Receita total
     - Taxa média de ocupação
   - Veja tabela detalhada de cada evento

## Funcionalidades

### Tela Inicial (index.html)
- ✅ Lista todos os eventos
- ✅ Busca por nome
- ✅ Barra de progresso de ocupação
- ✅ Links para detalhes

### Login/Cadastro (login.html)
- ✅ Cadastro com firstName/lastName
- ✅ Escolha de role (USER ou ORGANIZER)
- ✅ Login com JWT
- ✅ Validação de campos
- ✅ Mensagens de erro claras

### Detalhes do Evento (evento-detalhes.html)
- ✅ Todas as informações do evento
- ✅ Disponibilidade em tempo real
- ✅ Barra de ocupação visual
- ✅ Botão "Comprar" (se logado)
- ✅ Prompt de login (se não logado)

### Compra de Ingressos (comprar.html)
- ✅ Escolha de quantidade
- ✅ Formulários dinâmicos para cada participante
- ✅ Validação de CPF (11 dígitos)
- ✅ Cálculo automático do total
- ✅ Confirmação de compra

### Criar Evento (criar-evento.html)
- ✅ Formulário completo
- ✅ Validação de campos
- ✅ Apenas para ORGANIZER
- ✅ Redirecionamento para dashboard

### Dashboard (dashboard.html)
- ✅ Cards de estatísticas gerais
- ✅ Tabela de eventos com métricas
- ✅ Taxa de ocupação com cores
- ✅ Receita por evento
- ✅ Apenas para ORGANIZER

## Persistência de Dados

O frontend usa `localStorage` para armazenar:
- `jwt_token` - Token JWT de autenticação
- `user` - Dados do usuário (id, firstName, lastName, email, role)

**Logout:**
- Remove dados do localStorage
- Recarrega a página

## APIs Utilizadas

```javascript
// Auth Service (porta 8084)
POST   /users                              // Cadastro
POST   /auth/login/password                // Login

// Event Service (porta 8083)
GET    /api/events                         // Listar eventos
GET    /api/events/{id}                    // Detalhes
GET    /api/events/search?q={termo}        // Buscar
POST   /api/organizer/events               // Criar (ORGANIZER)
GET    /api/dashboard/organizer/{id}       // Dashboard (ORGANIZER)

// Ticket Service (porta 8085)
POST   /api/tickets/purchase               // Comprar (USER)
```

## Validações

### CPF
- Deve ter exatamente 11 dígitos numéricos
- Sem pontos, traços ou espaços
- Exemplo válido: `12345678909`

### Senha
- Mínimo 8 caracteres

### Data de Nascimento
- Formato: YYYY-MM-DD
- Campo tipo `date` do HTML5

## Estilos

O arquivo `style.css` inclui:
- Design responsivo (mobile-friendly)
- Cores modernas (#2c3e50, #3498db, #27ae60)
- Animações suaves
- Cards com sombra
- Barras de progresso
- Tabelas estilizadas
- Formulários bem organizados

## Cores de Status

**Taxa de Ocupação:**
- 🔴 Vermelho: 0-19%
- 🟠 Laranja: 20-49%
- 🟡 Amarelo: 50-79%
- 🟢 Verde: 80-100%

## Troubleshooting

### Erro de CORS
Se aparecer erro de CORS no console:
1. Use um servidor HTTP local (Python, http-server, Live Server)
2. Ou configure CORS no backend com `@CrossOrigin(origins = "*")`

### Serviço não responde
Verifique se os serviços estão rodando:
```bash
curl http://localhost:8084/users        # Auth
curl http://localhost:8083/api/events   # Events
curl http://localhost:8085/api/tickets  # Tickets
```

### Token expirado
Se aparecer erro 401:
1. Faça logout
2. Faça login novamente

### Dashboard vazio
Se o dashboard não mostrar dados:
1. Verifique se você criou eventos
2. Verifique se está logado como ORGANIZER
3. Verifique o console do navegador (F12)

## Melhorias Futuras

Possíveis melhorias para o frontend:

1. **Framework moderno** - React, Vue ou Angular
2. **Gerenciamento de estado** - Redux, Vuex
3. **Rotas** - React Router, Vue Router
4. **UI Library** - Material UI, Ant Design, Bootstrap
5. **Validação avançada** - Formik, Yup
6. **Gráficos** - Chart.js, Recharts
7. **Upload de imagens** - Para capa dos eventos
8. **Preview de ingresso** - Antes da compra
9. **Histórico de compras** - Para usuários
10. **Edição de eventos** - Para organizadores

---

**Status:** Frontend funcional e pronto para uso! ✅
