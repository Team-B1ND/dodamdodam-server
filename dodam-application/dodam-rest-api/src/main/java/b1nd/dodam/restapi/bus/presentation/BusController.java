package b1nd.dodam.restapi.bus.presentation;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import b1nd.dodam.restapi.bus.application.BusApplicationUseCase;
import b1nd.dodam.restapi.bus.application.BusQrcodeUseCase;
import b1nd.dodam.restapi.bus.application.BusUseCase;
import b1nd.dodam.restapi.bus.application.data.req.BusPresetReq;
import b1nd.dodam.restapi.bus.application.data.req.BusReq;
import b1nd.dodam.restapi.bus.application.data.req.BusWithPresetReq;
import b1nd.dodam.restapi.bus.application.data.res.BusMemberRes;
import b1nd.dodam.restapi.bus.application.data.res.BusQrcodeNonceRes;
import b1nd.dodam.restapi.bus.application.data.res.BusRes;
import b1nd.dodam.restapi.bus.application.data.res.BusSeatRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class BusController {

    private final BusUseCase busUseCase;
    private final BusApplicationUseCase busApplicationUseCase;
    private final BusQrcodeUseCase busQrcodeUseCase;

    @PostMapping
    public Response register(@RequestBody @Valid BusReq req) {
        return busUseCase.register(req);
    }

    @PostMapping("/{id}")
    public Response registerWithPreset(@PathVariable int id, @RequestBody @Valid BusWithPresetReq req) {
        return busUseCase.register(id, req);
    }

    @PostMapping("/preset")
    public Response registerPreset(@RequestBody @Valid BusPresetReq req) {
        return busUseCase.registerPreset(req);
    }

    // TODO 삭제하기
    @PostMapping("/apply/{id}")
    public Response apply(@PathVariable int id) {
        return busApplicationUseCase.apply(id);
    }

    @PostMapping("/qr-code/scan")
    public Response scanQRCode(
            HttpServletRequest httpServletReq,
            @RequestParam String memberId,
            @RequestParam String nonce
    ){
        return busQrcodeUseCase.scanBusQrcode(nonce, memberId, httpServletReq.getHeader("bus-api-key"));
    }

    @PatchMapping("/{id}")
    public Response modify(@PathVariable int id, @RequestBody BusReq req) {
        return busUseCase.modify(id, req);
    }

    // TODO 삭제하기
    @PatchMapping("/apply/{id}")
    public Response modifyApplication(@PathVariable int id) {
        return busApplicationUseCase.modify(id);
    }

    @PatchMapping("/apply/status/{id}/{seatNumber}")
    public Response modifyStatus(
            @PathVariable int id,
            @PathVariable(required = false) int seatNumber
    ) {
        return busApplicationUseCase.modifyStatus(id, seatNumber);
    }

    @PatchMapping("/status/{id}/{status}")
    public Response modifyStatus(
            @PathVariable int id,
            @PathVariable BusStatus status
    ){
        return busUseCase.modifyStatus(id, status);
    }

    @GetMapping
    public ResponseData<List<Bus>> getValid() {
        return busUseCase.getValid();
    }

    @GetMapping("/list")
    public ResponseData<List<BusRes>> getAll(@RequestParam int page, @RequestParam int limit) {
        return busUseCase.getAll(page, limit);
    }

    // TODO 삭제하기
    @GetMapping("/date")
    public ResponseData<List<BusRes>> getByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        return busUseCase.getByDate(year, month, day);
    }

    @GetMapping("/apply")
    public ResponseData<Bus> getMy() {
        return busUseCase.getMy();
    }

    @GetMapping("/{id}/seats")
    public ResponseData<BusSeatRes> getSeats(@PathVariable int id) {
        return busApplicationUseCase.getSeatNumbers(id);
    }

    @GetMapping("/{id}/seats/invalid")
    public ResponseData<BusSeatRes> getSeatsInvalid(@PathVariable int id) {
        return busApplicationUseCase.getSeatNumbersInvalid(id);
    }

    @GetMapping("/qr-code/nonce")
    public ResponseData<BusQrcodeNonceRes> getNonce() {
        return busQrcodeUseCase.issueNonce();
    }

    @GetMapping("/{id}/student/{status}")
    public ResponseData<List<BusMemberRes>> getApplicant(@PathVariable int id,
                                                         @PathVariable BusApplicationStatus status
    ){
        return busApplicationUseCase.getBusStudent(id, status);
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable int id) {
        return busUseCase.delete(id);
    }

    // TODO 삭제하기
    @DeleteMapping("/apply/{id}")
    public Response cancelApplication(@PathVariable int id) {
        return busApplicationUseCase.cancel();
    }

}
