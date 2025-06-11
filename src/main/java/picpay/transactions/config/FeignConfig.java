package picpay.transactions.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "picpay.transactions.client")
public class FeignConfig {
}