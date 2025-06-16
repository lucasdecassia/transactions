package picpay.transactions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import picpay.transactions.dto.TransactionDTO;
import picpay.transactions.model.Transaction;

public interface TransactionControllerAPI {

    @Operation(summary = "Realizar Transferência", description = "Efetua uma transferência entre usuários", operationId = "performTransaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferência realizada com sucesso", content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/transfer")
    ResponseEntity<Transaction> performTransaction(@RequestBody @Valid TransactionDTO transactionDTO);
}
