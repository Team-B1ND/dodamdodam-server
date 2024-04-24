package b1nd.dodamapi.auth.handler;

import b1nd.dodamapi.auth.usecase.AuthUseCase;
import b1nd.dodamapi.common.response.ResponseData;
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

    private final AuthUseCase useCase;

    @PostMapping("/login")
    public CompletableFuture<ResponseData<LoginRes>> login(@RequestBody @Valid LoginReq req) {
        return useCase.login(req);
    }

    @PostMapping("/reissue")
    public CompletableFuture<ResponseData<ReissueTokenRes>> reissue(@RequestBody @Valid ReissueTokenReq req) {
        return useCase.reissue(req);
    }

}
