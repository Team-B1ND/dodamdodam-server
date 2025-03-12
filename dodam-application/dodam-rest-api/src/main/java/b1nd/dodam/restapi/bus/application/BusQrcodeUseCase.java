package b1nd.dodam.restapi.bus.application;

import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.bus.exception.BusApiKeyInvalidException;
import b1nd.dodam.domain.rds.bus.repository.BusApplicationRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.redis.bus.service.BusRedisService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.bus.application.data.res.BusQrcodeNonceRes;
import b1nd.dodam.restapi.bus.properties.BusQRCodeProperties;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.util.RandomCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusQrcodeUseCase {
    private final BusApplicationRepository busApplicationRepository;
    private final MemberRepository memberRepository;
    private final BusRedisService redisService;
    private final MemberAuthenticationHolder authenticationHolder;
    private final BusQRCodeProperties properties;

    @Transactional(rollbackFor = Exception.class)
    public ResponseData<BusQrcodeNonceRes> issueNonce() {
        String nonce = RandomCode.UUIDRandomCode();
        Member member = authenticationHolder.current();
        redisService.cacheBusQRNonce(member.getId(), nonce);
        return ResponseData.ok("버스 QR 난수 생성 성공", new BusQrcodeNonceRes(nonce));
    }

    @Transactional(rollbackFor = Exception.class)
    public Response scanBusQrcode(String nonce, String memberId, String apiKey) {
        validateBusApiKey(apiKey);
        redisService.validateBusQRNonce(memberId, nonce);
        Member member = memberRepository.getById(memberId);
        BusApplication busApplication = busApplicationRepository.findByStatusAndStudent_Member(BusApplicationStatus.NOT_BOARDING, member);
        busApplication.updateStatus(BusApplicationStatus.BOARDING);
        redisService.evictBusQRNonce(memberId);
        return Response.ok("버스 QR 인증 성공");
    }

    private void validateBusApiKey(String apiKey) {
        if (!Objects.equals(properties.getApiKey(), apiKey)) {
            throw new BusApiKeyInvalidException();
        }
    }
}
