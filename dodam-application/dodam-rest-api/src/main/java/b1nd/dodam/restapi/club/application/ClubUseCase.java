package b1nd.dodam.restapi.club.application;

import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubUseCase {
    public Response save() {
        return Response.created("동아리 생성 완료");
    }
}
