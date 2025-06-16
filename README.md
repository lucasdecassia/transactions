
Este projeto é uma implementação simplificada do PicPay, permitindo transferências entre usuários e lojistas.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Web
- Spring Cloud OpenFeign
- Postgres Database
- Docker
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

## + Testes unitarios para os metodos 
