package b1nd.dodam.domain.redis.member.service;

import b1nd.dodam.domain.redis.member.enumeration.AuthType;
import b1nd.dodam.domain.redis.member.exception.AuthCodeNotMatchException;
import b1nd.dodam.domain.redis.member.exception.AuthInvalidException;
import b1nd.dodam.domain.redis.support.config.MemberRedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberAuthRedisService {

    private final MemberRedisProperties memberRedisProperties;
    private final StringRedisTemplate redisTemplate;
    private final int AUTH_CODE_EXPIRATION = 5;
    private final int AUTH_ACCESS_EXPIRATION = 15;

    public void updateAuthCode(AuthType authType, String identifier, int authCode) {
        String key = String.format("%s:%s:%s", memberRedisProperties.getCode().key(), authType, identifier);
        redisTemplate.opsForValue()
                .set(key, String.valueOf(authCode), Duration.ofMinutes(AUTH_CODE_EXPIRATION));
    }

    public void validateAuthCode(AuthType authType, String identifier, int authCode) {
        String key = String.format("%s:%s:%s", memberRedisProperties.getCode().key(), authType, identifier);
        String storedAuthCode = redisTemplate.opsForValue().get(key);

        if (storedAuthCode == null || Integer.parseInt(storedAuthCode) != authCode) {
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

    public void updateUserAgentValidation(String userAgent, AuthType authType, Optional<String> phone) {
        String key = memberRedisProperties.getAccess().key() + ":" + hashUserAgent(userAgent);

        if (AuthType.EMAIL.equals(authType) && phone.filter(p ->
                !Boolean.TRUE.equals(redisTemplate.hasKey(String.format("%s:%s:%s", memberRedisProperties.getCode().key(), AuthType.PHONE, p))))
                .isPresent()) {
            throw new AuthInvalidException();
        }

        redisTemplate.opsForHash().put(key, authType.name(), Boolean.TRUE.toString());
        redisTemplate.expire(key, Duration.ofMinutes(AUTH_ACCESS_EXPIRATION));
    }

    public void validateUserAuth(String userAgent, boolean requireEmail) {
        String key = memberRedisProperties.getAccess().key() + ":" + hashUserAgent(userAgent);
        Map<Object, Object> authData = redisTemplate.opsForHash().entries(key);

        if (authData.isEmpty() || !authData.containsKey(AuthType.PHONE.name())) {
            throw new AuthInvalidException();
        }

        if (requireEmail && !authData.containsKey(AuthType.EMAIL.name())) {
            throw new AuthInvalidException();
        }
    }

}
