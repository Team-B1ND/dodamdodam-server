package b1nd.dodamcore.auth.application.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerifyTokenResponse {

    private int statusCode;
    private String message;
    private VerifyToken data;

    @Getter
    public static class VerifyToken{
        private String memberId;
        private int accessLevel;
        private int apiKeyAccessLevel;
        private int iat;
        private int exp;
        private String iss;
        private String sub;
    }

}