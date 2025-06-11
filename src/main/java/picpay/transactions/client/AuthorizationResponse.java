package picpay.transactions.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse {
    private String status;
    private AuthorizationData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorizationData {
        private boolean authorization;
    }

    public boolean isAuthorized() {
        return data != null && data.isAuthorization();
    }
}
