package b1nd.dodamcore.member.application;

import b1nd.dodamcore.member.domain.entity.Member;

public interface MemberSessionHolder {

    Member current();

}