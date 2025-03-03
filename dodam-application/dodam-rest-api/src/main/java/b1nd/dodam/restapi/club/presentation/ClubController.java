package b1nd.dodam.restapi.club.presentation;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.restapi.club.application.ClubMemberUseCase;
import b1nd.dodam.restapi.club.application.ClubTimeUseCase;
import b1nd.dodam.restapi.club.application.ClubUseCase;
import b1nd.dodam.restapi.club.application.data.req.*;
import b1nd.dodam.restapi.club.application.data.res.*;
import b1nd.dodam.restapi.member.application.data.res.StudentWithImageRes;
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
    private final ClubTimeUseCase clubTimeUseCase;
    private final ClubMemberUseCase clubMemberUseCase;

    @PostMapping
    public Response create(
            @RequestBody @Valid CreateClubReq req
    ) {
        return clubUseCase.save(req);
    }

    @PostMapping("/pass")
    public Response passClub(
            @RequestBody PassClubReq req
    ) {
        return clubMemberUseCase.setPassClub(req);
    }

    @PostMapping("/join-requests")
    public Response joinRequest(
            @RequestBody @Valid List<JoinClubMemberReq> req
    ) {
        return clubMemberUseCase.joinClubs(req);
    }

    @PostMapping("/{id}/waiting")
    public Response setWaiting(
        @PathVariable Long id
    ) {
        return clubUseCase.setWaiting(id);
    }

    @PostMapping("/{id}/teacher")
    public Response setTeacher(
        @PathVariable Long id
    ) {
        return clubUseCase.setTeacher(id);
    }


    @PostMapping("/join-requests/{id}")
    public Response acceptClubJoinRequest(
            @PathVariable Long id
    ) {
        return clubMemberUseCase.updateClubJoinRequestReceived(id, ClubStatus.ALLOWED);
    }

    @PostMapping("/time")
    public Response setClubTime(
        @RequestBody ClubTimeReq req
    ) {
        return clubTimeUseCase.save(req);
    }

    @DeleteMapping("/{id}")
    public Response delete(
            @PathVariable Long id
    ) {
        return clubUseCase.delete(id);
    }

    @DeleteMapping("/join-requests/{id}")
    public Response rejectClubJoinRequest(
            @PathVariable Long id
    ) {
        return clubMemberUseCase.updateClubJoinRequestReceived(id, ClubStatus.REJECTED);
    }

    @PatchMapping("/{id}")
    public Response update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateClubInfoReq req
    ) {
        return clubUseCase.updateInfo(id, req);
    }

    @PatchMapping("/state")
    public Response state(
            @RequestBody @Valid UpdateClubReq req
    ) {
        return clubUseCase.update(req);
    }

    @GetMapping("/joined")
    public ResponseData<List<ClubStatusRes>> getJoinedClubs() {
        return clubMemberUseCase.getJoinedClubs();
    }

    @GetMapping("/members")
    public ResponseData<List<StudentWithImageRes>> getSecondGradeMembers() {
        return clubMemberUseCase.getSecondGradeStudents();
    }

    @GetMapping("/members/self")
    public ResponseData<List<StudentWithImageRes>> getAllMembers() {
        return clubMemberUseCase.getAllGradeStudents();
    }

    @GetMapping
    public ResponseData<List<ClubDetailRes>> getClubs() {
        return clubUseCase.getClubs();
    }

    @GetMapping("/{id}")
    public ResponseData<ClubDetailRes> getClubDetail(
            @PathVariable Long id
    ) {
        return clubUseCase.getClubDetail(id);
    }

    @GetMapping("{id}/leader")
    public ResponseData<ClubStudentRes> getClubLeader(
            @PathVariable Long id
    ) {
        return clubMemberUseCase.getClubLeader(id);
    }

    @GetMapping("/time")
    public ResponseData<ClubTimeRes> getClubTime() {
        return clubTimeUseCase
    }

    @GetMapping("/join-requests/received")
    public ResponseData<List<ClubMemberRes>> getJoinRequests() {
        return clubMemberUseCase.getClubJoinRequestsReceived();
    }

    @GetMapping("/{id}/all-members")
    public ResponseData<List<ClubStudentRes>> getAllClubMembers(
            @PathVariable Long id
    ) {
        return clubMemberUseCase.getAllClubMembers(id);
    }

    @GetMapping("/{id}/members")
    public ResponseData<List<ClubStudentRes>> getClubMembers(
            @PathVariable Long id
    ) {
        return clubMemberUseCase.getActiveClubMembers(id);
    }

    @GetMapping("/my")
    public ResponseData<List<ClubDetailRes>> getMyClubStatus() {
        return clubMemberUseCase.getStudentClubStatus();
    }
}
