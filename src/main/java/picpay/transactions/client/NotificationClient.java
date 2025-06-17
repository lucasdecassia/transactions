package picpay.transactions.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import picpay.transactions.notification.NotificationRequest;
import picpay.transactions.notification.NotificationResponse;

@FeignClient(name = "notification-service", url = "${notification.service.url:https://util.devi.tools/api/v1/notify}")
public interface NotificationClient {

    @PostMapping
    NotificationResponse sendNotification(@RequestBody NotificationRequest request);
}
