package b1nd.dodam.restapi.auth.presentation;

import b1nd.dodam.restapi.auth.application.AuthUseCase;
import b1nd.dodam.restapi.auth.application.data.req.LoginReq;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.auth.application.data.req.ReissueTokenReq;
import b1nd.dodam.restapi.auth.application.data.res.LoginRes;
import b1nd.dodam.restapi.auth.application.data.res.ReissueTokenRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping("/reissue")
//    public CompletableFuture<ResponseData<ReissueTokenRes>> reissue(@RequestBody @Valid ReissueTokenReq req) {
//        return useCase.reissue(req);
//    }

}
