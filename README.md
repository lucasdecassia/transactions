# PicPay Simplificado

Este projeto é uma implementação simplificada do PicPay, permitindo transferências entre usuários e lojistas.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Web
- Spring Cloud OpenFeign
- H2 Database
- Lombok
- Flyway

## Funcionalidades

- Transferência de dinheiro entre usuários
- Validação de saldo e tipo de usuário
- Autorização de transação via API externa
- Notificação de recebimento de transferência

## Estrutura do Projeto

```
src/main/java/picpay/transactions
├── client        # Clientes Feign para APIs externas
├── config        # Configurações da aplicação
├── controller    # Controllers REST
├── dto           # Objetos de transferência de dados
├── exception     # Exceções personalizadas
├── model         # Entidades JPA
├── repository    # Repositórios Spring Data
└── service       # Serviços de negócio
```

## Regras de Negócio

1. Usuários podem ser do tipo COMMON ou MERCHANT
2. Apenas usuários do tipo COMMON podem realizar transferências
3. Usuários devem ter saldo suficiente para realizar transferências
4. Todas as transferências devem ser autorizadas por um serviço externo
5. O beneficiário deve ser notificado ao receber uma transferência

## Endpoints

### Realizar Transferência

```
POST /api/transactions

{
  "payerId": 1,
  "payeeId": 3,
  "amount": 100.00
}
```

## Dados Iniciais

A aplicação já vem com alguns usuários cadastrados:

1. João Silva (COMMON) - ID: 1
   - Email: joao.silva@example.com
   - Saldo: R$ 1000,00

2. Maria Souza (COMMON) - ID: 2
   - Email: maria.souza@example.com
   - Saldo: R$ 800,00

3. Loja do José (MERCHANT) - ID: 3
   - Email: loja.jose@example.com
   - Saldo: R$ 5000,00

4. Mercado do Pedro (MERCHANT) - ID: 4
   - Email: mercado.pedro@example.com
   - Saldo: R$ 3000,00

## Como Executar

1. Clone o repositório
2. Execute `./mvnw spring-boot:run`
3. Acesse a aplicação em `http://localhost:8080`
4. Acesse o console H2 em `http://localhost:8080/h2-console`
   - JDBC URL: jdbc:h2:mem:picpaydb
   - Usuário: sa
   - Senha: password

## Testes

Execute `./mvnw test` para rodar os testes unitários.