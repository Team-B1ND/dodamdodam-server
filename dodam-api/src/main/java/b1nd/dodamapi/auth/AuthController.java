package b1nd.dodamapi.auth;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.auth.application.AuthService;
import b1nd.dodamcore.auth.application.dto.req.LoginReq;
import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.auth.application.dto.res.ReissueTokenRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseData<LoginRes> login(@RequestBody @Valid LoginReq req) {
        return ResponseData.ok("로그인 성공", authService.login(req).join());
    }

    @PostMapping("/reissue")
    public ResponseData<ReissueTokenRes> reissue(@RequestBody @Valid ReissueTokenReq req) {
        return ResponseData.ok("토큰 재발급 성공", authService.reissue(req).join());
    }

}