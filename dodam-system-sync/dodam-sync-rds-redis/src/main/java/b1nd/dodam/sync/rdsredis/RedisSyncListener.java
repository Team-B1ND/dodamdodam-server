package b1nd.dodam.sync.rdsredis;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.redis.member.service.MemberSearchRedisService;
import b1nd.dodam.sync.rdsredis.model.MemberInfoSyncModel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSyncListener {

    private final MemberSearchRedisService memberSearchRedisService;

    @Async
    @EventListener
    public void handleSyncEvent(Object entity) {
        if (entity instanceof Member member) {
            memberSearchRedisService.updateMemberInfoWithIndex(MemberInfoSyncModel.toRedisModel(member, null, null));
        } else if (entity instanceof Student student) {
            memberSearchRedisService.updateMemberInfoWithIndex(MemberInfoSyncModel.toRedisModel(student.getMember(), student, null));
        } else if (entity instanceof Teacher teacher) {
            memberSearchRedisService.updateMemberInfoWithIndex(MemberInfoSyncModel.toRedisModel(teacher.getMember(), null, teacher));
        }
    }
}