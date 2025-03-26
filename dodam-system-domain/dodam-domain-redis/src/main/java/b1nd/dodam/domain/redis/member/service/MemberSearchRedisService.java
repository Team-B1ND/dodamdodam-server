package b1nd.dodam.domain.redis.member.service;

import b1nd.dodam.domain.redis.member.model.MemberInfoRedisModel;
import b1nd.dodam.domain.redis.support.RedisearchCommandsSupport;
import com.redis.lettucemod.search.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberSearchRedisService {
    private final RedisearchCommandsSupport redisCommandsSupport;
    private final String MEMBER_INFO_INDEX = "memberInfoIndex";
    private final String MEMBER_INFO_KEY_PREFIX = "memberInfo:";

    public List<MemberInfoRedisModel> searchMembers(String keyword, Integer grade, String role, String status, long page, long pageSize) {
        String gradeFilter = (grade != null) ? String.format("@studentGrade:[%d %d]", grade, grade) : "";
        String roleFilter = (role != null) ? String.format("@role:'%s'", role) : "";  // 따옴표 추가
        String statusFilter = (status != null) ? String.format("@status:'%s'", status) : "";  // 따옴표 추가

        String query = String.format(
                "@name:*%s*' %s %s %s",
                keyword, gradeFilter, roleFilter, statusFilter
        );

        System.out.println(query);
        Optional<SearchOptions<String, String>> options = Optional.of(SearchOptions.<String, String>builder()

                .limit((page - 1) * pageSize, pageSize)
                .build());

        SearchResults<String, String> results = redisCommandsSupport.search(MEMBER_INFO_INDEX, Optional.of(query), options);
        return MemberInfoRedisModel.convertToMemberInfoRedisModel(results);
    }

    public List<MemberInfoRedisModel> getAllMember() {
        SearchResults<String, String> results = redisCommandsSupport.search(MEMBER_INFO_INDEX, Optional.empty(), Optional.empty());
        return MemberInfoRedisModel.convertToMemberInfoRedisModel(results);
    }

    public void addMemberInfo(MemberInfoRedisModel model) {
        String key = MEMBER_INFO_KEY_PREFIX + model.id();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        Map<String, String> memberMap = new HashMap<>();
        memberMap.put("id", model.id());
        memberMap.put("name", model.name());
        memberMap.put("email", model.email());
        memberMap.put("role", model.role());
        memberMap.put("status", model.status());
        memberMap.put("profileImage", model.profileImage());
        memberMap.put("phone", model.phone());
        memberMap.put("studentId", String.valueOf(model.studentId()));
        memberMap.put("studentGrade", String.valueOf(model.studentGrade()));
        memberMap.put("studentRoom", String.valueOf(model.studentRoom()));
        memberMap.put("studentNumber", String.valueOf(model.studentNumber()));
        memberMap.put("studentCode", model.studentCode());
        memberMap.put("teacherTel", model.teacherTel());
        memberMap.put("teacherPosition", model.teacherPosition());
        memberMap.put("createdAt", model.createdAt() != null ? formatter.format(model.createdAt()) : null);
        memberMap.put("modifiedAt", model.modifiedAt() != null ? formatter.format(model.modifiedAt()) : null);

        redisCommandsSupport.addHset(key, memberMap);
    }

    public void updateMemberInfoWithIndex(MemberInfoRedisModel model) {
        String key = MEMBER_INFO_KEY_PREFIX + model.id();
        redisCommandsSupport.deleteFromIndex(MEMBER_INFO_INDEX, key);
        addMemberInfo(model);
    }
}
