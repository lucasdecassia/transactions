CREATE TABLE tb_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    document VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL,
    user_type VARCHAR(20) NOT NULL
);

CREATE TABLE tb_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payer_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (payer_id) REFERENCES tb_users(id),
    FOREIGN KEY (payee_id) REFERENCES tb_users(id)
);