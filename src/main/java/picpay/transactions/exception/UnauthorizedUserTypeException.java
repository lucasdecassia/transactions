package picpay.transactions.exception;

/**
 * Exceção lançada quando um usuário não está autorizado a realizar uma transação devido ao seu tipo.
 */
public class UnauthorizedUserTypeException extends RuntimeException {
    
    public UnauthorizedUserTypeException(String message) {
        super(message);
    }
}