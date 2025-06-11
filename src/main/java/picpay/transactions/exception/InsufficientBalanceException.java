package picpay.transactions.exception;

/**
 * Exceção lançada quando o usuário não tem saldo suficiente para realizar uma transação.
 */
public class InsufficientBalanceException extends RuntimeException {
    
    public InsufficientBalanceException(String message) {
        super(message);
    }
}