package picpay.transactions.exception;

/**
 * Exceção lançada quando uma transação não é autorizada pelo serviço de autorização.
 */
public class TransactionNotAuthorizedException extends RuntimeException {
    
    public TransactionNotAuthorizedException(String message) {
        super(message);
    }
}