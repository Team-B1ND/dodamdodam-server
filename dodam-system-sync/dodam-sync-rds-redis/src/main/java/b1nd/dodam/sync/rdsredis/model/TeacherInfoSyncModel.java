package b1nd.dodam.sync.rdsredis.model;

import b1nd.dodam.domain.rds.member.entity.Teacher;

public record TeacherInfoSyncModel(
            int id, String name, String tel, String position
    ) {
        public static TeacherInfoSyncModel of(Teacher teacher) {
            if(teacher == null) {
                return null;
            }
            return new TeacherInfoSyncModel(teacher.getId(), teacher.getMember().getName(), teacher.getTel(), teacher.getPosition());
        }
    }
