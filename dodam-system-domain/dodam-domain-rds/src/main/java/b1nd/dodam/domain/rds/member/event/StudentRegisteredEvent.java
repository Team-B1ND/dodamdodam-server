package b1nd.dodam.domain.rds.member.event;

import b1nd.dodam.domain.rds.member.entity.Student;

public record StudentRegisteredEvent(Student student) {}
