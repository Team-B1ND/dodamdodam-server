package b1nd.dodam.restapi.nightstudy.presentation;

import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.restapi.nightstudy.application.NightStudyUseCase;
import b1nd.dodam.restapi.nightstudy.application.data.req.ApplyNightStudyProjectReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.ApplyNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.BanNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.req.RejectNightStudyReq;
import b1nd.dodam.restapi.nightstudy.application.data.res.*;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/night-study")
@RequiredArgsConstructor
public class NightStudyController {

    private final NightStudyUseCase useCase;

    @PostMapping
    public Response apply(@RequestBody @Valid ApplyNightStudyReq req) {
        return useCase.apply(req);
    }

    @PostMapping("/ban")
    public Response applyBan(@RequestBody @Valid BanNightStudyReq req) {
        return useCase.applyBan(req);
    }

    @PostMapping("/project")
    public Response applyProject(@RequestBody @Valid ApplyNightStudyProjectReq req) {
        return useCase.applyProject(req);
    }

    @DeleteMapping("/{id}")
    public Response cancel(@PathVariable Long id) {
        return useCase.cancel(id);
    }

    @DeleteMapping("/ban")
    public Response cancelBan(@RequestParam(name = "student") @NotNull int studentId) {
        return useCase.cancelBan(studentId);
    }

    @DeleteMapping("/project/{id}")
    public Response cancelProject(@PathVariable Long id) {
        return useCase.cancelProject(id);
    }

    @PatchMapping("/{id}/allow")
    public Response allow(@PathVariable Long id) {
        return useCase.allow(id);
    }

    @PatchMapping("/{id}/reject")
    public Response reject(@PathVariable Long id, @RequestBody Optional<RejectNightStudyReq> req) {
        return useCase.reject(id, req);
    }

    @PatchMapping("/{id}/revert")
    public Response revert(@PathVariable Long id) {
        return useCase.revert(id);
    }

    @PatchMapping("/project/{id}/allow/{room}")
    public Response allowProject(@PathVariable Long id, @PathVariable NightStudyProjectRoom room) {
        return useCase.allowProject(id, room);
    }

    @PatchMapping("/project/{id}/reject")
    public Response rejectProject(@PathVariable Long id, @RequestBody Optional<RejectNightStudyReq> req) {
        return useCase.rejectProject(id, req);
    }

    @PatchMapping("/project/{id}/revert")
    public Response revertProject(@PathVariable Long id) {
        return useCase.revertProject(id);
    }

    @GetMapping
    public ResponseData<List<NightStudyRes>> getValid() {
        return useCase.getValid();
    }

    @GetMapping("/all")
    public ResponseData<List<NightStudyRes>> getAllValid() {
        return useCase.getAll();
    }

    @GetMapping("/my")
    public ResponseData<List<NightStudyRes>> getMy() {
        return useCase.getMy();
    }

    @GetMapping("/ban/students")
    public ResponseData<List<StudentWithNightStudyBanRes>> getMembers() {
        return useCase.getMembers();
    }

    @GetMapping("/pending")
    public ResponseData<List<NightStudyRes>> getPending() {
        return useCase.getPending();
    }

    @GetMapping("/ban/my")
    public Response getMyBan() {
        return useCase.getMyBan();
    }

    @GetMapping("/bans")
    public ResponseData<List<NightStudyBanRes>> getAllBans() {
        return useCase.getAllActiveBans();
    }

    @GetMapping("/projects")
    public ResponseData<List<NightStudyProjectRes>> getAllValidProjects() {
        return useCase.getValidProjects();
    }

    @GetMapping("/project/{id}")
    public ResponseData<NightStudyProjectWithMembersRes> getProjectById(@PathVariable Long id) {
        return useCase.getProjectDetails(id);
    }

    @GetMapping("/project/my")
    public ResponseData<List<NightStudyProjectRes>> getMyProjects() {
        return useCase.getMyProjects();
    }

    @GetMapping("/project/allowed")
    public ResponseData<List<NightStudyProjectWithMembersRes>> getAllowedProjects() {
        return useCase.getAllowedProjects();
    }

    @GetMapping("/project/pending")
    public ResponseData<List<NightStudyProjectWithMembersRes>> getPendingProjects() {
        return useCase.getPendingProjects();
    }

    @GetMapping("/project/rooms")
    public ResponseData<List<NightStudyProjectRoomRes>> getUsingProjectRooms() {
        return useCase.getRoomsInUse();
    }

    @GetMapping("/students")
    public ResponseData<List<StudentWithNightStudyBanRes>> getAvailableStudents() {
        return useCase.getStudentsWithBan();
    }

    @GetMapping("/project/students")
    public ResponseData<List<StudentWithNightStudyProjectRes>> getValidProjectStudents() {
        return useCase.getProjectStudents();
    }
}
