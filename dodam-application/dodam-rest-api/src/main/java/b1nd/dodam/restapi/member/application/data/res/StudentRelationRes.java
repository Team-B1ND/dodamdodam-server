package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.StudentRelation;

public record StudentRelationRes(
        String profileImage,
        String name,
        String relation
) {
    public static StudentRelationRes of(StudentRelation studentRelation){
        return new StudentRelationRes(
                studentRelation.getStudent().getMember().getProfileImage(),
                studentRelation.getStudent().getMember().getName(),
                studentRelation.getRelation()
        );
    }
}
