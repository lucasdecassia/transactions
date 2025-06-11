package picpay.transactions.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authorization-service", url = "${authorization.service.url:https://util.devi.tools/api/v2/authorize}")
public interface AuthorizationClient {

    @GetMapping("/")
    AuthorizationResponse authorize();
}
