package b1nd.dodam.restapi.support.encrypt;

import b1nd.dodam.core.exception.global.InternalServerException;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

@Slf4j
public final class Sha512PasswordEncoder {

    private Sha512PasswordEncoder() {}

    public static String encode(String rawPassword) {
        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(rawPassword.getBytes());

            for (byte byteDatum : md.digest()) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception e) {
            log.error("Encrypt Exception : " + e.getClass());
            log.error("Encrypt Exception Message : " + e.getMessage());

            throw new InternalServerException();
        }
    }

    public static boolean matches(String rawPassword, String encryptedPassword) {
        return encryptedPassword.equals(encode(rawPassword));
    }

}
