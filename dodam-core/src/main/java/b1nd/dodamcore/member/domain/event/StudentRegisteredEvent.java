package b1nd.dodamcore.member.domain.event;

import b1nd.dodamcore.member.domain.entity.Student;

public record StudentRegisteredEvent(Student student) {}