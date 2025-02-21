package b1nd.dodam.restapi.member.application.data.res;

import java.util.List;

public record ParentRes(
        int id,
        String name,
        List<StudentRelationRes> studentRelationRes
) {
}
