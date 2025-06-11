package picpay.transactions.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${notification.service.url:https://util.devi.tools/api/v1/notify}")
public interface NotificationClient {

    @PostMapping
    NotificationResponse sendNotification(@RequestBody NotificationRequest request);
}
