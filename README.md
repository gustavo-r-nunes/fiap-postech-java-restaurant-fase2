# Restaurant Management API - Tech Challenge Fase 2

API REST desenvolvida em Java com Spring Boot para gerenciamento de usuários, tipos de usuários, restaurantes e itens de cardápio.

Este projeto faz parte da Fase 2 do Tech Challenge e contempla:

- Cadastro e gerenciamento de tipos de usuário.
- Associação de usuários aos tipos `Dono de Restaurante` e `Cliente`.
- Cadastro e gerenciamento de restaurantes.
- Associação de restaurantes a usuários responsáveis.
- Cadastro e gerenciamento de itens do cardápio.
- Associação de itens do cardápio a restaurantes.
- Documentação da API via Swagger/OpenAPI.
- Collection Postman para testes manuais dos endpoints.
- Testes unitários e testes de integração.
- Cobertura mínima de testes configurada com JaCoCo.
- Execução integrada com Docker Compose e PostgreSQL.
- Organização em camadas inspirada em Clean Architecture.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Docker
- Docker Compose
- Swagger/OpenAPI
- JUnit 5
- Mockito
- Testcontainers
- JaCoCo
- Maven

---

## Arquitetura

O projeto está organizado em camadas, seguindo princípios de Clean Architecture e separação de responsabilidades.

```text
src/main/java/com/restaurant/management
├── application
│   ├── dto
│   │   ├── input
│   │   └── output
│   ├── gateway
│   └── usecase
├── domain
│   ├── entity
│   └── exception
├── infrastructure
│   ├── config
│   └── persistence
│       ├── entity
│       ├── gateway
│       ├── mapper
│       └── repository
└── presentation
    ├── controller
    ├── dto
    │   ├── request
    │   └── response
    ├── exception
    ├── mapper
    └── presenter
```

### Responsabilidades das camadas

| Camada | Responsabilidade |
|---|---|
| `domain` | Contém as entidades principais e exceções de domínio. |
| `application` | Contém os casos de uso, regras de aplicação, DTOs internos e interfaces de gateway. |
| `infrastructure` | Contém persistência, entidades JPA, repositórios, mappers e implementações dos gateways. |
| `presentation` | Contém controllers REST, DTOs de request/response, presenters, mappers e tratamento global de exceções. |

---

## Entidades principais

### UserType

Representa o tipo de usuário.

Campos:

- `id`
- `name`

Tipos utilizados pela aplicação:

- `Dono de Restaurante`
- `Cliente`

A aplicação inicializa automaticamente esses dois tipos padrão caso ainda não existam no banco de dados.

---

### User

Representa um usuário do sistema.

Campos:

- `id`
- `name`
- `email`
- `userType`

A associação entre usuário e tipo de usuário é feita através do campo `userTypeId` nas requisições da API.

---

### Restaurant

Representa um restaurante.

Campos:

- `id`
- `name`
- `address`
- `cuisineType`
- `openingHours`
- `owner`

Regras de negócio:

- O dono do restaurante deve ser um usuário existente.
- O usuário informado como dono deve ser do tipo `Dono de Restaurante`.

---

### MenuItem

Representa um item do cardápio de um restaurante.

Campos:

- `id`
- `name`
- `description`
- `price`
- `onlyAvailableInRestaurant`
- `photoPath`
- `restaurant`

Observações:

- O campo `photoPath` armazena apenas o caminho onde a foto estaria salva.
- Não há upload real de arquivo nesta fase.

---

## Endpoints da API

Base URL local:

```text
http://localhost:8080
```

---

## Tipos de usuário

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/user-types` | Cria um tipo de usuário |
| `GET` | `/api/user-types` | Lista todos os tipos de usuário |
| `GET` | `/api/user-types/{id}` | Busca um tipo de usuário por ID |
| `PUT` | `/api/user-types/{id}` | Atualiza um tipo de usuário |
| `DELETE` | `/api/user-types/{id}` | Remove um tipo de usuário |

### Criar tipo de usuário

```json
{
  "name": "Administrador"
}
```

### Resposta esperada

```json
{
  "id": 1,
  "name": "Administrador"
}
```

---

## Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/users` | Cria um usuário |
| `GET` | `/api/users` | Lista todos os usuários |
| `GET` | `/api/users/{id}` | Busca um usuário por ID |
| `PUT` | `/api/users/{id}` | Atualiza um usuário |
| `DELETE` | `/api/users/{id}` | Remove um usuário |

### Criar usuário

```json
{
  "name": "João Dono",
  "email": "joao.dono@email.com",
  "userTypeId": 1
}
```

### Resposta esperada

```json
{
  "id": 1,
  "name": "João Dono",
  "email": "joao.dono@email.com",
  "userTypeId": 1,
  "userTypeName": "Dono de Restaurante"
}
```

---

## Restaurantes

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/restaurants` | Cria um restaurante |
| `GET` | `/api/restaurants` | Lista todos os restaurantes |
| `GET` | `/api/restaurants/{id}` | Busca um restaurante por ID |
| `PUT` | `/api/restaurants/{id}` | Atualiza um restaurante |
| `DELETE` | `/api/restaurants/{id}` | Remove um restaurante |

### Criar restaurante

```json
{
  "name": "Cantina Bella",
  "address": "Rua das Flores, 123",
  "cuisineType": "Italiana",
  "openingHours": "Segunda a sábado, 11h às 23h",
  "ownerId": 1
}
```

### Resposta esperada

```json
{
  "id": 1,
  "name": "Cantina Bella",
  "address": "Rua das Flores, 123",
  "cuisineType": "Italiana",
  "openingHours": "Segunda a sábado, 11h às 23h",
  "ownerId": 1,
  "ownerName": "João Dono"
}
```

### Regra de negócio

Para criar um restaurante, o campo `ownerId` deve apontar para um usuário existente do tipo:

```text
Dono de Restaurante
```

Caso o usuário informado seja do tipo `Cliente`, a API retorna erro `400 Bad Request`.

---

## Itens do cardápio

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/menu-items` | Cria um item do cardápio |
| `GET` | `/api/menu-items` | Lista todos os itens do cardápio |
| `GET` | `/api/menu-items/{id}` | Busca um item do cardápio por ID |
| `GET` | `/api/menu-items/restaurant/{restaurantId}` | Lista os itens de um restaurante |
| `PUT` | `/api/menu-items/{id}` | Atualiza um item do cardápio |
| `DELETE` | `/api/menu-items/{id}` | Remove um item do cardápio |

### Criar item do cardápio

```json
{
  "name": "Pizza Margherita",
  "description": "Pizza com molho de tomate, mussarela e manjericão",
  "price": 49.90,
  "onlyAvailableInRestaurant": false,
  "photoPath": "/images/pizza-margherita.jpg",
  "restaurantId": 1
}
```

### Resposta esperada

```json
{
  "id": 1,
  "name": "Pizza Margherita",
  "description": "Pizza com molho de tomate, mussarela e manjericão",
  "price": 49.90,
  "onlyAvailableInRestaurant": false,
  "photoPath": "/images/pizza-margherita.jpg",
  "restaurantId": 1,
  "restaurantName": "Cantina Bella"
}
```

---

## Códigos de resposta

| Código | Descrição |
|---:|---|
| `200 OK` | Requisição executada com sucesso |
| `201 Created` | Recurso criado com sucesso |
| `204 No Content` | Recurso removido com sucesso |
| `400 Bad Request` | Dados inválidos ou regra de negócio violada |
| `404 Not Found` | Recurso não encontrado |
| `409 Conflict` | Conflito de dados, como registro duplicado ou vínculo existente |

---

## Tratamento de erros

A API utiliza `ProblemDetail` para padronizar as respostas de erro.

### Exemplo de erro de validação

```json
{
  "type": "/errors/validation",
  "title": "Erro de validação",
  "status": 400,
  "detail": "name: O nome do restaurante é obrigatório"
}
```

### Exemplo de recurso não encontrado

```json
{
  "type": "/errors/not-found",
  "title": "Recurso não encontrado",
  "status": 404,
  "detail": "Restaurante não encontrado"
}
```

### Exemplo de regra de negócio inválida

```json
{
  "type": "/errors/bad-request",
  "title": "Requisição inválida",
  "status": 400,
  "detail": "O usuário informado deve ser do tipo Dono de Restaurante"
}
```

---

## Swagger/OpenAPI

A documentação interativa da API está disponível via Swagger UI.

Com a aplicação em execução, acesse:

```text
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI em JSON fica disponível em:

```text
http://localhost:8080/api-docs
```

Configurações utilizadas:

```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Collection Postman

A collection para testes manuais dos endpoints está disponível em:

```text
postman/restaurant-management-api.postman_collection.json
```

A collection contempla fluxos para:

- Tipos de usuário
- Usuários
- Restaurantes
- Itens do cardápio
- Limpeza dos dados criados durante o teste

Recomendações:

- Execute a collection com a aplicação em execução.
- Execute preferencialmente em uma base limpa para evitar conflitos de dados duplicados.

---

## Configuração da aplicação

Arquivo principal:

```text
src/main/resources/application.properties
```

Configuração padrão:

```properties
spring.application.name=management

server.port=8080

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/restaurant_management}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:restaurant}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:restaurant}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Banco de dados

O projeto utiliza PostgreSQL.

Configurações padrão:

| Propriedade | Valor |
|---|---|
| Banco | `restaurant_management` |
| Usuário | `restaurant` |
| Senha | `restaurant` |
| Porta | `5432` |

---

## Como executar com Docker Compose

Pré-requisitos:

- Docker
- Docker Compose

Na raiz do projeto, execute:

```powershell
docker compose up --build
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

O PostgreSQL ficará disponível em:

```text
localhost:5432
```

Para parar os containers:

```powershell
docker compose down
```

Para parar os containers e remover o volume do banco:

```powershell
docker compose down -v
```

---

## Como executar localmente

Pré-requisitos:

- Java 21
- PostgreSQL
- Maven ou Maven Wrapper

Antes de executar localmente, garanta que exista um banco PostgreSQL com as configurações:

| Propriedade | Valor |
|---|---|
| Banco | `restaurant_management` |
| Usuário | `restaurant` |
| Senha | `restaurant` |
| Porta | `5432` |

Na raiz do projeto, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Ou, se estiver usando Maven instalado na máquina:

```powershell
mvn spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

---

## Como executar os testes

Para executar os testes automatizados:

```powershell
.\mvnw.cmd test
```

Ou com Maven instalado:

```powershell
mvn test
```

---

## Cobertura de testes

O projeto utiliza JaCoCo para geração de relatório de cobertura.

Para executar os testes e gerar o relatório:

```powershell
.\mvnw.cmd clean verify
```

Ou com Maven instalado:

```powershell
mvn clean verify
```

O relatório será gerado em:

```text
target/site/jacoco/index.html
```

A cobertura mínima configurada no projeto é de:

```text
80%
```

Essa regra está configurada no `pom.xml` através do plugin `jacoco-maven-plugin`.

---

## Testes de integração

O projeto possui testes de integração utilizando:

- Spring Boot Test
- Testcontainers
- PostgreSQL Container
- RestClient

Esses testes validam o fluxo real da aplicação com banco PostgreSQL em container, incluindo:

- Criação de tipo de usuário.
- Criação de usuário dono.
- Criação de restaurante.
- Criação de item de cardápio.
- Validação de regra de negócio para impedir restaurante com usuário cliente.
- Validação de erro ao criar item para restaurante inexistente.

Observação:

- Para executar os testes de integração, o Docker precisa estar em execução, pois o projeto utiliza Testcontainers.

---

## Docker Compose

O arquivo `docker-compose.yml` sobe dois serviços:

| Serviço | Descrição |
|---|---|
| `postgres` | Banco PostgreSQL da aplicação |
| `app` | Aplicação Java Spring Boot |

O serviço da aplicação depende do banco estar saudável antes de iniciar.

---

## Dockerfile

O projeto utiliza build multi-stage:

1. Imagem Maven com Java 21 para gerar o `.jar`.
2. Imagem JRE Java 21 para executar a aplicação.

Isso reduz o tamanho da imagem final e separa a etapa de build da etapa de execução.

---

## Validações implementadas

### Tipo de usuário

- `name` obrigatório.
- `name` entre 2 e 80 caracteres.
- Não permite nomes duplicados ignorando maiúsculas/minúsculas.

### Usuário

- `name` obrigatório.
- `email` obrigatório.
- `email` com formato válido.
- `userTypeId` obrigatório e positivo.
- `userTypeId` deve apontar para um tipo de usuário existente.

### Restaurante

- `name` obrigatório.
- `address` obrigatório.
- `cuisineType` obrigatório.
- `openingHours` obrigatório.
- `ownerId` obrigatório e positivo.
- `ownerId` deve apontar para um usuário existente.
- O usuário dono deve ser do tipo `Dono de Restaurante`.

### Item do cardápio

- `name` obrigatório.
- `description` obrigatória.
- `price` obrigatório.
- `price` deve ser maior que zero.
- `photoPath` pode ser vazio, iniciar com `/` ou ser uma URL `http/https`.
- `restaurantId` obrigatório e positivo.
- `restaurantId` deve apontar para um restaurante existente.

---

## Fluxo sugerido para teste manual

### 1. Listar tipos de usuário

```http
GET /api/user-types
```

Use o retorno para identificar o ID do tipo `Dono de Restaurante`.

---

### 2. Criar um usuário dono

```http
POST /api/users
```

```json
{
  "name": "João Dono",
  "email": "joao.dono@email.com",
  "userTypeId": 1
}
```

---

### 3. Criar um restaurante

```http
POST /api/restaurants
```

```json
{
  "name": "Cantina Bella",
  "address": "Rua das Flores, 123",
  "cuisineType": "Italiana",
  "openingHours": "Segunda a sábado, 11h às 23h",
  "ownerId": 1
}
```

---

### 4. Criar um item do cardápio

```http
POST /api/menu-items
```

```json
{
  "name": "Pizza Margherita",
  "description": "Pizza com molho de tomate, mussarela e manjericão",
  "price": 49.90,
  "onlyAvailableInRestaurant": false,
  "photoPath": "/images/pizza-margherita.jpg",
  "restaurantId": 1
}
```

---

### 5. Listar os itens do restaurante

```http
GET /api/menu-items/restaurant/1
```

---

## Aderência aos requisitos da Fase 2

| Requisito | Status |
|---|---|
| CRUD de tipo de usuário | Implementado |
| Associação de usuário com tipo de usuário | Implementado |
| CRUD de restaurantes | Implementado |
| Associação de restaurante com usuário dono | Implementado |
| Validação de dono como `Dono de Restaurante` | Implementado |
| CRUD de itens do cardápio | Implementado |
| Associação de item do cardápio com restaurante | Implementado |
| Campo de caminho da foto do prato | Implementado |
| Docker Compose com aplicação e banco | Implementado |
| Documentação do projeto | Implementado |
| Swagger/OpenAPI | Implementado |
| Collection Postman | Implementado |
| Clean Architecture/camadas | Implementado |
| Testes unitários | Implementado |
| Testes de integração | Implementado |
| Cobertura mínima de 80% configurada | Implementado |

---

## Observações importantes

- A aplicação inicializa automaticamente os tipos `Dono de Restaurante` e `Cliente`.
- Para cadastrar um restaurante, o usuário informado em `ownerId` deve ser do tipo `Dono de Restaurante`.
- O campo `photoPath` representa apenas o caminho onde a foto do prato estaria armazenada.
- A API está aberta, sem autenticação, para facilitar os testes acadêmicos da fase.
- A collection do Postman deve ser executada preferencialmente em uma base limpa.
- Para validar a cobertura mínima configurada, execute `mvn clean verify` ou `.\mvnw.cmd clean verify`.
- Para executar os testes de integração, mantenha o Docker em execução.

---

## Licença

Este projeto é acadêmico e foi desenvolvido para o Tech Challenge da FIAP.