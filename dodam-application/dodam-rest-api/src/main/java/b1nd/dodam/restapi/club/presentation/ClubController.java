package b1nd.dodam.restapi.club.presentation;

import b1nd.dodam.restapi.club.application.ClubUseCase;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubUseCase clubUseCase;

    @PostMapping
    public Response create(
            @RequestBody CreateClubReq req,
            @RequestParam List<Integer> studentIds
    ) {
        return clubUseCase.save(req, studentIds);
    }
}
