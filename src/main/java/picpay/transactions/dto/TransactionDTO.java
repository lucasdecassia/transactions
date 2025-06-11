package picpay.transactions.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @NotNull(message = "O ID do pagador é obrigatório")
    private Long payer;

    @NotNull(message = "O ID do beneficiário é obrigatório")
    private Long payee;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal value;
}
