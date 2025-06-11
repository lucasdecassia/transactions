package picpay.transactions.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picpay.transactions.client.AuthorizationClient;
import picpay.transactions.client.AuthorizationResponse;
import picpay.transactions.client.NotificationClient;
import picpay.transactions.client.NotificationResponse;
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
import picpay.transactions.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Service Tests")
class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationClient authorizationClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User payer;
    private User payee;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        payer = new User();
        payer.setId(1L);
        payer.setName("Pagador");
        payer.setEmail("pagador@brasil.com");
        payer.setDocument("12345678900");
        payer.setBalance(new BigDecimal("100.00"));
        payer.setUserType(UserType.COMMON);

        payee = new User();
        payee.setId(2L);
        payee.setName("Beneficiário");
        payee.setEmail("beneficiario@picpay.com");
        payee.setDocument("00987654321");
        payee.setBalance(new BigDecimal("50.00"));
        payee.setUserType(UserType.MERCHANT);

        transactionDTO = new TransactionDTO();
        transactionDTO.setPayer(1L);
        transactionDTO.setPayee(2L);
        transactionDTO.setValue(new BigDecimal("10.00"));
    }

    @Test
    @DisplayName("Deve realizar uma transação com sucesso quando todas as condições estiverem validas")
    void performTransaction_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(authorizationClient.authorize()).thenReturn(new AuthorizationResponse("success", new AuthorizationResponse.AuthorizationData(true)));
        when(notificationClient.sendNotification(any())).thenReturn(new NotificationResponse("Notificação enviada", true));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.performTransaction(transactionDTO);

        assertThat(transaction, is(notNullValue()));
        assertThat(transaction.getPayer(), is(equalTo(payer)));
        assertThat(transaction.getPayee(), is(equalTo(payee)));
        assertThat(transaction.getAmount(), is(equalTo(new BigDecimal("10.00"))));
        assertThat(payer.getBalance(), is(equalTo(new BigDecimal("90.00"))));
        assertThat(payee.getBalance(), is(equalTo(new BigDecimal("60.00"))));

        verify(userRepository).save(payer);
        verify(userRepository).save(payee);
        verify(transactionRepository).save(any(Transaction.class));
        verify(notificationClient).sendNotification(any());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o pagador não for encontrado")
    void performTransaction_PayerNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            transactionService.performTransaction(transactionDTO);
        });

        assertThat(exception.getMessage(), containsString("Pagador não encontrado"));

        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o beneficiário não for encontrado")
    void performTransaction_PayeeNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            transactionService.performTransaction(transactionDTO);
        });

        assertThat(exception.getMessage(), containsString("Beneficiário não encontrado"));

        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedUserTypeException quando o pagador for um comerciante")
    void performTransaction_UnauthorizedUserType() {
        payer.setUserType(UserType.MERCHANT);
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        Exception exception = assertThrows(UnauthorizedUserTypeException.class, () -> {
            transactionService.performTransaction(transactionDTO);
        });

        assertThat(exception.getMessage(), containsString("Apenas usuários comuns podem realizar transferências"));

        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Deve lançar InsufficientBalanceException quando o pagador tiver saldo insuficiente")
    void performTransaction_InsufficientBalance() {
        transactionDTO.setValue(new BigDecimal("200.00"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.performTransaction(transactionDTO);
        });

        assertThat(exception.getMessage(), containsString("Saldo insuficiente"));

        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Deve lançar TransactionNotAuthorizedException quando a transação não for autorizada")
    void performTransaction_TransactionNotAuthorized() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(authorizationClient.authorize()).thenReturn(new AuthorizationResponse("fail", new AuthorizationResponse.AuthorizationData(false)));

        Exception exception = assertThrows(TransactionNotAuthorizedException.class, () -> {
            transactionService.performTransaction(transactionDTO);
        });

        assertThat(exception.getMessage(), containsString("Transação não autorizada"));

        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Deve lançar TransactionNotAuthorizedException quando o serviço de autorização lançar uma exceção")
    void performTransaction_AuthorizationError() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(authorizationClient.authorize()).thenThrow(new RuntimeException("Erro de conexão"));

        // Execute & Verify
        Exception exception = assertThrows(TransactionNotAuthorizedException.class, () -> {
            transactionService.performTransaction(transactionDTO);
        });

        assertThat(exception.getMessage(), containsString("Erro ao autorizar a transação"));
        assertThat(exception.getMessage(), containsString("Erro de conexão"));

        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
