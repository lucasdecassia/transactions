package picpay.transactions.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {
    private String message;
    private boolean sent;
}