package picpay.transactions.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String message;
    private boolean sent;
}