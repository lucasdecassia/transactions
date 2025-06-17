package picpay.transactions.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class NotificationRequest {
    private String email;
    private String message;
}