package picpay.transactions.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    @NotNull(message = "O pagador é obrigatório")
    private User payer;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    @NotNull(message = "O beneficiário é obrigatório")
    private User payee;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal amount;

    @NotNull(message = "A data é obrigatória")
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}