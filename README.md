# 🛒 E-commerce API

API REST que simula uma loja virtual com gerenciamento de **produtos**, **categorias** e **upload de imagens**.

Este projeto foi desenvolvido como parte do meu portfólio para demonstrar habilidades com **Java Spring Boot**, **Spring Data JPA**, **validações**, **tratamento de erros**, **upload de arquivos**, **storage local**, **filtros de busca** e boas práticas de API.

---

## 🚀 Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Data JPA Specifications
- Hibernate
- Maven
- PostgreSQL
- Swagger / OpenAPI
- Bean validation
- Lombok

---

## 📦 Estrutura do projeto

```
src/
├── main/
│   ├── java/com/loja/e_commerce
│   │   ├── configs
│   │   ├── controllers
│   │   ├── docs
│   │   ├── dtos
│   │   ├── exceptions
│   │   ├── mapper
│   │   ├── models
│   │   ├── repositories
│   │   ├── services
│   │   └── ECommerceApplication.java
│   └── resources
│       ├── static/
│       ├── templates/
│       ├── application-example.properties
├── test/
uploads/
pom.xml
```

- construção de API pronta para evolução e escalabilidade

---

## 🛠️ Configuração

Copie o arquivo de exemplo:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

---

## 🗄️ Banco de dados

O projeto utiliza banco relacional (ex: PostgreSQL).

### 1. Criar o banco

No PostgreSQL:

```sql
CREATE DATABASE ecommerce;
```

### 2. Configurar conexão

Edite o arquivo:

```
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> O Hibernate criará as tabelas automaticamente.

---

## ▶️ Como rodar

Clone o projeto:

```bash
git clone https://github.com/Lucas-dev23/e-commerce-api.git
cd e-commerce-api
```

Execute:

```bash
./mvnw spring-boot:run
```

API rodando em:

```
http://localhost:8080
```

---

## 📡 Endpoints

### 📦 Produtos

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /produtos | Criar produto |
| PUT | /produtos/{id} | Atualizar produto |
| GET | /produtos | Listar produtos com filtros |
| GET | /produtos/{id} | Buscar por ID |
| DELETE | /produtos/{id} | Excluir produto |
| POST | /produtos/{id}/imagem | Upload de imagem |

Filtros disponíveis:

- nome parcial
- preço mínimo
- preço máximo
- categoria
- paginação

---

### 🏷 Categorias

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /categorias | Criar categoria |
| PUT | /categorias/{id} | Atualizar categoria |
| GET | /categorias | Listar categorias |
| GET | /categorias/{id} | Buscar por ID |

---

## 📄 Documentação Swagger

A API possui documentação automática.

Após rodar o projeto, acesse:

```
http://localhost:8080/swagger-ui.html
```

ou

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📁 Upload de imagens

- Suporta JPG e PNG
- Máximo: 2MB
- Armazenamento local
- Substitui imagem antiga automaticamente

Diretório configurável via:

```
storage.upload-dir
```

A pasta de uploads está ignorada no Git.

---

## ⚠️ Tratamento de erros

A API utiliza exceptions customizadas:

- 400 → BadRequest
- 404 → ResourceNotFound
- 409 → Conflict
- 500 → erro interno

---

## 🧪 Testes

Ainda não implementados.

Planejado para versões futuras.

---

## 🛠 Melhorias futuras

- Testes automatizados
- Autenticação JWT
- Docker
- Checkout de pedido (Pagamento simulado)

---

## 📌 Objetivo do projeto

Demonstrar:

- arquitetura limpa em camadas
- separação de responsabilidades
- DTO + Mapper
- validações de negócio
- paginação e filtros
- upload de arquivos
- documentação Swagger
- boas práticas com Spring Boot

---

## 👨‍💻 Autor

Lucas Lima  
GitHub: https://github.com/Lucas-dev23
