package b1nd.dodam.restapi.member.application.data.req;

import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.StudentRelation;

public record ConnectStudentReq (String code, String relation){

    public StudentRelation mapToStudentRelation(Student student, Parent parent){
        return StudentRelation.builder()
                .relation(relation)
                .student(student)
                .parent(parent)
                .build();
    }

}
