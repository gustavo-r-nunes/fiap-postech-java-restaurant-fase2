# Restaurant Management API

API REST desenvolvida em Java com Spring Boot para gerenciamento de usuários, tipos de usuário, restaurantes e itens de cardápio.

Este projeto faz parte da Fase 2 do Tech Challenge e contempla:

- Cadastro e gerenciamento de tipos de usuário.
- Associação de usuários a tipos como `Dono de Restaurante` e `Cliente`.
- Cadastro e gerenciamento de restaurantes.
- Associação de restaurante a um usuário dono.
- Cadastro e gerenciamento de itens do cardápio.
- Associação de itens do cardápio a restaurantes.
- Documentação via Swagger/OpenAPI.
- Testes unitários e de integração.
- Cobertura de testes com JaCoCo.
- Execução integrada com Docker Compose e PostgreSQL.
- Organização em camadas inspirada em Clean Architecture.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Security
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

O projeto está organizado em camadas, buscando separação de responsabilidades e maior facilidade de manutenção.

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

- `domain`: contém as entidades principais e exceções de domínio.
- `application`: contém os casos de uso, DTOs internos e interfaces de gateway.
- `infrastructure`: contém persistência, repositórios, configurações e integrações técnicas.
- `presentation`: contém controllers REST, DTOs de request/response, mappers, presenters e tratamento de exceções.

---

## Entidades principais

### UserType

Representa o tipo de usuário.

Campos:

- `id`
- `name`

Exemplos cadastrados automaticamente pela aplicação:

- `Dono de Restaurante`
- `Cliente`

### User

Representa um usuário do sistema.

Campos:

- `id`
- `name`
- `email`
- `userType`

O usuário é associado a um tipo através do campo `userTypeId` nas requisições da API.

### Restaurant

Representa um restaurante.

Campos:

- `id`
- `name`
- `address`
- `cuisineType`
- `openingHours`
- `owner`

Regra de negócio:

- O dono do restaurante deve ser um usuário existente do tipo `Dono de Restaurante`.

### MenuItem

Representa um item do cardápio.

Campos:

- `id`
- `name`
- `description`
- `price`
- `onlyAvailableInRestaurant`
- `photoPath`
- `restaurant`

O campo `photoPath` armazena apenas o caminho da imagem do prato.

---

## Endpoints da API

Base URL:

```text
http://localhost:8080
```

### Tipos de usuário

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/user-types` | Cria um tipo de usuário |
| `GET` | `/api/user-types` | Lista todos os tipos de usuário |
| `GET` | `/api/user-types/{id}` | Busca um tipo de usuário por ID |
| `PUT` | `/api/user-types/{id}` | Atualiza um tipo de usuário |
| `DELETE` | `/api/user-types/{id}` | Remove um tipo de usuário |

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/users` | Cria um usuário |
| `GET` | `/api/users` | Lista todos os usuários |
| `GET` | `/api/users/{id}` | Busca um usuário por ID |
| `PUT` | `/api/users/{id}` | Atualiza um usuário |
| `DELETE` | `/api/users/{id}` | Remove um usuário |

### Restaurantes

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/restaurants` | Cria um restaurante |
| `GET` | `/api/restaurants` | Lista todos os restaurantes |
| `GET` | `/api/restaurants/{id}` | Busca um restaurante por ID |
| `PUT` | `/api/restaurants/{id}` | Atualiza um restaurante |
| `DELETE` | `/api/restaurants/{id}` | Remove um restaurante |

### Itens do cardápio

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/menu-items` | Cria um item do cardápio |
| `GET` | `/api/menu-items` | Lista todos os itens do cardápio |
| `GET` | `/api/menu-items/{id}` | Busca um item do cardápio por ID |
| `GET` | `/api/menu-items/restaurant/{restaurantId}` | Lista os itens de um restaurante |
| `PUT` | `/api/menu-items/{id}` | Atualiza um item do cardápio |
| `DELETE` | `/api/menu-items/{id}` | Remove um item do cardápio |

---

## Exemplos de payload

### Criar tipo de usuário

```json
{
  "name": "Administrador"
}
```

Observação: a aplicação já inicializa automaticamente os tipos `Dono de Restaurante` e `Cliente`.

---

### Criar usuário

```json
{
  "name": "João Dono",
  "email": "joao.dono@email.com",
  "userTypeId": 1
}
```

---

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

Regra de negócio:

- O campo `ownerId` deve apontar para um usuário existente do tipo `Dono de Restaurante`.

---

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

---

## Como executar com Docker Compose

Pré-requisitos:

- Docker
- Docker Compose

Na pasta do projeto:

```powershell
cd "management\management"
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

Configurações padrão do banco:

| Propriedade | Valor |
|---|---|
| Banco | `restaurant_management` |
| Usuário | `restaurant` |
| Senha | `restaurant` |
| Porta | `5432` |

Para parar os containers:

```powershell
docker compose down
```

---

## Como executar localmente

Pré-requisitos:

- Java 21
- PostgreSQL
- Maven ou Maven Wrapper

Antes de executar a aplicação localmente, garanta que existe um banco PostgreSQL com as seguintes configurações:

| Propriedade | Valor |
|---|---|
| Banco | `restaurant_management` |
| Usuário | `restaurant` |
| Senha | `restaurant` |
| Porta | `5432` |

Execute:

```powershell
cd "management\management"
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

---

## Configuração da aplicação

As principais configurações estão no arquivo:

```text
src/main/resources/application.properties
```

Configuração principal:

```properties
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

## Swagger/OpenAPI

Com a aplicação em execução, acesse:

```text
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI também fica disponível em:

```text
http://localhost:8080/api-docs
```

---

## Collection Postman

A collection para testar os endpoints está disponível em:

```text
postman/restaurant-management-api.postman_collection.json
```

Ela contempla fluxos para:

- Tipos de usuário
- Usuários
- Restaurantes
- Itens do cardápio

Antes de executar a collection, suba a aplicação com Docker Compose ou localmente.

---

## Testes automatizados

O projeto possui testes unitários e testes de integração.

Para executar os testes:

```powershell
cd "management\management"
.\mvnw.cmd test
```

Para executar os testes e gerar relatório de cobertura:

```powershell
cd "management\management"
.\mvnw.cmd clean verify
```

O relatório JaCoCo será gerado em:

```text
target/site/jacoco/index.html
```

A cobertura mínima configurada no projeto é de 80%.

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

## Observações importantes

- A aplicação inicializa automaticamente os tipos `Dono de Restaurante` e `Cliente`.
- Para cadastrar um restaurante, o usuário informado em `ownerId` deve ser do tipo `Dono de Restaurante`.
- O campo `photoPath` representa apenas o caminho onde a foto do prato estaria armazenada.
- Os endpoints estão liberados sem autenticação para facilitar os testes acadêmicos da fase.
- A collection do Postman deve ser executada preferencialmente em uma base limpa para evitar conflitos de dados duplicados.


