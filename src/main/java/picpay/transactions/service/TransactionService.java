package picpay.transactions.service;

import picpay.transactions.dto.TransactionDTO;
import picpay.transactions.model.Transaction;


public interface TransactionService {

    Transaction performTransaction(TransactionDTO transactionDTO);
}