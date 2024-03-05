package b1nd.dodamapi.auth;

import b1nd.dodamcore.auth.application.AuthService;
import b1nd.dodamcore.auth.application.dto.req.LoginReq;
import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.auth.application.dto.res.ReissueTokenRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CompletableFuture<LoginRes> login(@RequestBody @Valid LoginReq req) {
        return authService.login(req);
    }

    @PostMapping("/reissue")
    public CompletableFuture<ReissueTokenRes> reissue(@RequestBody @Valid ReissueTokenReq req) {
        return authService.reissue(req);
    }

}