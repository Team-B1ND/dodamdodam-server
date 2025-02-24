package b1nd.dodam.restapi.club.presentation;

import b1nd.dodam.restapi.club.application.ClubMemberUseCase;
import b1nd.dodam.restapi.club.application.ClubUseCase;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.club.application.data.req.UpdateClubInfoReq;
import b1nd.dodam.restapi.club.application.data.res.ClubDetailRes;
import b1nd.dodam.restapi.club.application.data.res.ClubMemberRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubUseCase clubUseCase;
    private final ClubMemberUseCase clubMemberUseCase;

    @PostMapping
    public Response create(
            @RequestBody @Valid CreateClubReq req
    ) {
        return clubUseCase.save(req);
    }

    @DeleteMapping("/{id}")
    public Response delete(
            @PathVariable Long id
    ) {
        return clubUseCase.delete(id);
    }

    @PatchMapping("/{id}")
    public Response update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateClubInfoReq req
    ) {
        return clubUseCase.update(id, req);
    }

    @GetMapping("/{id}")
    public ResponseData<ClubDetailRes> getClubDetail(
            @PathVariable Long id
    ) {
        return clubUseCase.getClubDetail(id);
    }

    @GetMapping("/join-requests/received")
    public ResponseData<List<ClubMemberRes>> getJoinRequests() {
        return clubMemberUseCase.getClubJoinRequestsReceived();
    }
}
