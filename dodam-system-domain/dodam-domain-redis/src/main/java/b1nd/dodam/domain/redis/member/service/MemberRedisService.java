package b1nd.dodam.domain.redis.member.service;

import b1nd.dodam.domain.redis.member.enumeration.AuthType;
import b1nd.dodam.domain.redis.member.exception.AuthCodeNotMatchException;
import b1nd.dodam.domain.redis.member.exception.AuthInvalidException;
import b1nd.dodam.domain.redis.support.config.MemberRedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRedisService {

    private final MemberRedisProperties memberRedisProperties;
    private final StringRedisTemplate redisTemplate;
    private final int AUTH_CODE_EXPIRATION = 5;
    private final int AUTH_ACCESS_EXPIRATION = 15;

    public void updateAuthCode(AuthType authType, String identifier, int authCode) {
        String key = String.format("%s:%s:%s", memberRedisProperties.getCode().key(), authType, identifier);
        redisTemplate.opsForValue()
                .set(key, String.valueOf(authCode), Duration.ofMinutes(AUTH_CODE_EXPIRATION));
        log.info("{}", authCode);
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

    public void validateUserAgent(String userAgent) {
        String key = memberRedisProperties.getAccess().key() + ":" + hashUserAgent(userAgent);

        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new AuthInvalidException();
        }

        if (!redisTemplate.opsForHash().hasKey(key, AuthType.EMAIL.name()) ||
                !redisTemplate.opsForHash().hasKey(key, AuthType.PHONE.name())) {
            throw new AuthInvalidException();
        }
    }

}
