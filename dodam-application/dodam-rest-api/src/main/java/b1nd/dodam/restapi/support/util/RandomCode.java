package b1nd.dodam.restapi.support.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class RandomCode {

    private static final SecureRandom random = new SecureRandom();

    public static int randomCode() {
        return 100_000 + random.nextInt(900_000);
    }


    public static String studentRandomCode() {
        StringBuilder stringBuilder = new StringBuilder();
        String random = UUID.randomUUID().toString();
        byte[] uuid = random.getBytes(StandardCharsets.UTF_8);
        byte[] hashBytes;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hashBytes = messageDigest.digest(uuid);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }

        for (int j = 0; j < 4; j++) {
            stringBuilder.append(String.format("%02x", hashBytes[j]));
        }

        return stringBuilder.toString().toUpperCase();
    }

}
