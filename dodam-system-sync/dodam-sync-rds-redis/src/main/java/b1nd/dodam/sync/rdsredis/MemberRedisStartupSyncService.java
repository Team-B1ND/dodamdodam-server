package b1nd.dodam.sync.rdsredis;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.member.repository.TeacherRepository;
import b1nd.dodam.domain.redis.member.model.MemberInfoRedisModel;
import b1nd.dodam.domain.redis.member.service.MemberSearchRedisService;
import b1nd.dodam.sync.rdsredis.model.MemberInfoSyncModel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Order(10)
@RequiredArgsConstructor
public class MemberRedisStartupSyncService implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberSearchRedisService memberSearchRedisService;

    @Override
    public void run(ApplicationArguments args) {
        syncDataOnStartup();
    }

    private void syncDataOnStartup() {
        List<Member> members = memberRepository.findAll();
        List<MemberInfoRedisModel> redisData = memberSearchRedisService.getAllMember();

        List<MemberInfoSyncModel> memberInfoResList = getMemberInfo(members);

        for (MemberInfoSyncModel memberInfo : memberInfoResList) {
            MemberInfoRedisModel newRedisModel = MemberInfoSyncModel.toRedisModel(memberInfo);
            MemberInfoRedisModel existingRedisModel = findExistingRedisModel(redisData, memberInfo.id());
            if (existingRedisModel == null || !existingRedisModel.equals(newRedisModel)) {
                memberSearchRedisService.addMemberInfo(newRedisModel);
            }
        }
    }

    private List<MemberInfoSyncModel> getMemberInfo(List<Member> members) {
        List<String> memberIds = members.stream()
                .map(Member::getId)
                .toList();
        List<Student> students = studentRepository.findByMemberIdIn(memberIds);
        List<Teacher> teachers = teacherRepository.findByMemberIdIn(memberIds);

        return members.parallelStream()
                .map(member -> {
                    Student student = findStudentByMemberId(students, member.getId());
                    Teacher teacher = findTeacherByMemberId(teachers, member.getId());
                    return MemberInfoSyncModel.of(member, student, teacher);
                })
                .toList();
    }

    private Student findStudentByMemberId(List<Student> students, String memberId) {
        return students.stream()
                .filter(student -> student.getMember().getId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    private Teacher findTeacherByMemberId(List<Teacher> teachers, String memberId) {
        return teachers.stream()
                .filter(teacher -> teacher.getMember().getId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    private MemberInfoRedisModel findExistingRedisModel(List<MemberInfoRedisModel> redisData, String memberId) {
        return redisData.stream()
                .filter(redisModel -> redisModel.id().equals(memberId))
                .findFirst()
                .orElse(null);
    }
}
