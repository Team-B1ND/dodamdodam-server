package b1nd.dodam.domain.redis.member.service;

import b1nd.dodam.domain.redis.member.exception.AuthCodeNotMatchException;
import b1nd.dodam.domain.redis.member.exception.MemberInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfraService {

    private final StringRedisTemplate redisTemplate;

    @Value("${auth.code.expiration}")
    private long AUTH_CODE_EXPIRATION;

    public void updateAuthCode(String type, String identifier, int authCode) {
        String key = String.format("verify:%s:%s", type, identifier);
        redisTemplate.opsForValue()
                .set(key, String.valueOf(authCode), Duration.ofMillis(AUTH_CODE_EXPIRATION));
    }

    public void validateAuthCode(String identifier, int authCode, String type){
        String key = String.format("verify:%s:%s", type, identifier);
        int storedAuthCode = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));

        if (storedAuthCode != authCode) {
            throw new AuthCodeNotMatchException();
        }
    }

    private String hashUserAgent(String userAgent) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(userAgent.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public boolean updateUserAgentValidation(String userAgent, String type) {
        String key = "session:" + hashUserAgent(userAgent);

        if (type.equals("EMAIL")) {
            return redisTemplate.opsForHash().hasKey(key, "PHONE");
        } else {
            Map<String, String> verificationStatus = new HashMap<>();
            verificationStatus.put(type, String.valueOf(Boolean.TRUE));

            redisTemplate.opsForHash().putAll(key, verificationStatus);
            redisTemplate.expire(key, Duration.ofMinutes(15));

            return true;
        }
    }

    public void validateUserAgent(String userAgent) {
        String key = "session:" + hashUserAgent(userAgent);

        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new MemberInvalidException();
        }

        if (!redisTemplate.opsForHash().hasKey(key, "EMAIL") || !redisTemplate.opsForHash().hasKey(key, "PHONE")) {
            throw new MemberInvalidException();
        }
    }

}
