package b1nd.dodam.restapi.member.application.data.res;

import b1nd.dodam.domain.rds.member.entity.Teacher;

public record TeacherRes(String name, String tel, String position) {
    public static TeacherRes of(Teacher teacher) {
        if(teacher == null) {
            return null;
        }
        return new TeacherRes(teacher.getMember().getName(), teacher.getTel(), teacher.getPosition());
    }
}
