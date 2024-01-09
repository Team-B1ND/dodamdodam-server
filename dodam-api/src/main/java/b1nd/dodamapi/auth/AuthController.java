package b1nd.dodamapi.auth;

import b1nd.dodamcore.auth.application.AuthService;
import b1nd.dodamcore.auth.application.dto.request.LoginRequest;
import b1nd.dodamcore.auth.application.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CompletableFuture<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}