# BookingMaster

BookingMaster é um sistema simples de gerenciamento de reservas de hotéis. Ele permite que usuários façam reservas, adicionem acompanhantes e consultem informações sobre hotéis e reservas.

## Funcionalidades

- **Gerenciamento de Usuários**:
  - Cadastro de usuários.
  - Consulta de informações de usuários.
  - Exclusão de usuários.

- **Gerenciamento de Hotéis**:
  - Cadastro de hotéis.
  - Consulta de hotéis disponíveis.
  - Exclusão de hotéis.

- **Gerenciamento de Reservas**:
  - Criação de reservas.
  - Consulta de reservas por usuário.
  - Cancelamento de reservas.

- **Relatórios**:
  - Geração de relatórios simples, como número de reservas por hotel.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.2.1**
- **MariaDB 11.2** (banco de dados via Docker)
- **Lombok**
- **JUnit 5** (para testes unitários)
- **Maven**

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── gramos22/
│   │           └── bookingmaster/
│   │               ├── controllers/  # Controladores REST
│   │               ├── dtos/         # Objetos de Transferência de Dados
│   │               ├── entities/     # Entidades JPA
│   │               ├── repositories/ # Repositórios JPA
│   │               ├── services/     # Lógica de Negócio
│   │               └── utils/        # Classes Utilitárias
│   └── resources/
│       ├── application.properties    # Configurações do Spring Boot
└── test/
    └── java/
        └── com/
            └── gramos22/
                └── bookingmaster/    # Testes Unitários
```

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/gramos22/bookingmaster.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd bookingmaster
   ```

3. Execute o projeto com o Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Acesse a aplicação em: `http://localhost:8080`

## Endpoints da API

### Usuários
- **POST /users**: Adicionar um novo usuário.
- **GET /users/{id}**: Consultar um usuário pelo ID.
- **DELETE /users/{id}**: Excluir um usuário pelo ID.

### Hotéis
- **POST /hotels**: Adicionar um novo hotel.
- **GET /hotels/{id}**: Consultar um hotel pelo ID.
- **GET /hotels**: Listar todos os hotéis.
- **DELETE /hotels/{id}**: Excluir um hotel pelo ID.

### Reservas
- **POST /bookings**: Criar uma nova reserva.
- **GET /bookings/{id}**: Consultar uma reserva pelo ID.
- **GET /bookings**: Listar todas as reservas.
- **DELETE /bookings/{id}**: Cancelar uma reserva pelo ID.

## Testes

Os testes unitários estão localizados no diretório `src/test/java`. Para executá-los, utilize o comando:
```bash
./mvnw test
```

## Autor

- **Gramos22**

---

Este projeto foi desenvolvido como um exemplo simples para fins de aprendizado e demonstração.