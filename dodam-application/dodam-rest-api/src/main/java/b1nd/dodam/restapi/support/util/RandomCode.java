package b1nd.dodam.restapi.support.util;

import java.security.SecureRandom;
import java.util.UUID;

public class RandomCode {

    private static final SecureRandom random = new SecureRandom();

    public static int randomCode() {
        return 100_000 + random.nextInt(900_000);
    }


    public static String UUIDRandomCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

}
