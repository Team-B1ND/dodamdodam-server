package b1nd.dodamapi.security.util;

import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.common.exception.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
@Slf4j
final class Sha512PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
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

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return encryptedPassword.equals(encode(rawPassword));
    }

}
