package picpay.transactions.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import picpay.transactions.client.AuthorizationClient;
import picpay.transactions.client.NotificationClient;
import picpay.transactions.notification.NotificationRequest;
import picpay.transactions.dto.TransactionDTO;
import picpay.transactions.exception.InsufficientBalanceException;
import picpay.transactions.exception.TransactionNotAuthorizedException;
import picpay.transactions.exception.UnauthorizedUserTypeException;
import picpay.transactions.exception.UserNotFoundException;
import picpay.transactions.model.Transaction;
import picpay.transactions.model.User;
import picpay.transactions.model.enums.UserType;
import picpay.transactions.repository.TransactionRepository;
import picpay.transactions.repository.UserRepository;
import picpay.transactions.service.TransactionService;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationClient authorizationClient;
    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public Transaction performTransaction(TransactionDTO transactionDTO) {
        User payer = userRepository.findById(transactionDTO.getPayer())
                .orElseThrow(() -> new UserNotFoundException("Pagador não encontrado"));

        User payee = userRepository.findById(transactionDTO.getPayee())
                .orElseThrow(() -> new UserNotFoundException("Beneficiário não encontrado"));

        validation(transactionDTO, payer);

        payer.setBalance(payer.getBalance().subtract(transactionDTO.getValue()));
        payee.setBalance(payee.getBalance().add(transactionDTO.getValue()));

        userRepository.save(payer);
        userRepository.save(payee);

        Transaction transaction = new Transaction();
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transaction.setAmount(transactionDTO.getValue());

        transaction = transactionRepository.save(transaction);

        notification(transactionDTO, payee, payer);

        return transaction;
    }

    private void notification(TransactionDTO transactionDTO, User payee, User payer) {
        try {
            notificationClient.sendNotification(new NotificationRequest(
                    payee.getEmail(),
                    "Você recebeu uma transferência de " + payer.getName() + " no valor de " + transactionDTO.getValue()));
        } catch (Exception e) {
            System.out.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }

    private void validation(TransactionDTO transactionDTO, User payer) {
        if (payer.getUserType() != UserType.COMMON) {
            throw new UnauthorizedUserTypeException("Apenas usuários comuns podem realizar transferências");
        }

        if (payer.getBalance().compareTo(transactionDTO.getValue()) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para realizar a transação");
        }

        try {
            var authorizationResponse = authorizationClient.authorize();
            if (!authorizationResponse.isAuthorized()) {
                throw new TransactionNotAuthorizedException("Transação não autorizada");
            }
        } catch (Exception e) {
            throw new TransactionNotAuthorizedException("Erro ao autorizar a transação: " + e.getMessage());
        }
    }
}
