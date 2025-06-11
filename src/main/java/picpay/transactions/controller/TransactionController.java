package picpay.transactions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import picpay.transactions.dto.TransactionDTO;
import picpay.transactions.model.Transaction;
import picpay.transactions.service.TransactionService;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> performTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        Transaction transaction = service.performTransaction(transactionDTO);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
