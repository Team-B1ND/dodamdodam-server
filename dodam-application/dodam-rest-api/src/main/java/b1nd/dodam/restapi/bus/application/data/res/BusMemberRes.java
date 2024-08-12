package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.BusApplication;

public record BusMemberRes(String memberId, String name, String email, String phone) {

    public static BusMemberRes createFromBusMember(BusApplication busApplication) {
        return new BusMemberRes(
                busApplication.getStudent().getMember().getId(),
                busApplication.getStudent().getMember().getName(),
                busApplication.getStudent().getMember().getEmail(),
                busApplication.getStudent().getMember().getPhone()
        );
    }
}
