package b1nd.dodam.restapi.member.application.data.req;

public record UpdateStudentForAdminReq(String pw, String name, String phone, String parentPhone, Integer grade,
                                       Integer room, Integer number) {}
