package b1nd.dodamcore.member.domain.vo;

import b1nd.dodamcore.member.domain.entity.Teacher;

public record TeacherRes(String name, String tel, String position) {
    public static TeacherRes of(Teacher teacher) {
        if(teacher == null) {
            return null;
        }
        return new TeacherRes(teacher.getMember().getName(), teacher.getTel(), teacher.getPosition());
    }
}
