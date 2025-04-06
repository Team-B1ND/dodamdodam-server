package b1nd.dodam.domain.redis.member.model;

import b1nd.dodam.core.util.NullSafeParserUtil;
import com.redis.lettucemod.search.Document;
import com.redis.lettucemod.search.SearchResults;

import java.time.LocalDateTime;
import java.util.List;

public record MemberInfoRedisModel(
        String id,
        String name,
        String email,
        String role,
        String status,
        String profileImage,
        String phone,

        Integer studentId,
        Integer studentGrade,
        Integer studentRoom,
        Integer studentNumber,
        String studentCode,

        Integer teacherId,
        String teacherTel,
        String teacherPosition,

        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static MemberInfoRedisModel convertToMemberInfoRedisModel(Document<String, String> doc) {
        return new MemberInfoRedisModel(
                doc.get("id"),
                doc.get("name"),
                doc.get("email"),
                doc.get("role"),
                doc.get("status"),
                doc.get("profileImage"),
                doc.get("phone"),

                NullSafeParserUtil.parseInt(doc.get("studentId")),
                NullSafeParserUtil.parseInt(doc.get("studentGrade")),
                NullSafeParserUtil.parseInt(doc.get("studentRoom")),
                NullSafeParserUtil.parseInt(doc.get("studentNumber")),
                doc.get("studentCode"),

                NullSafeParserUtil.parseInt(doc.get("teacherId")),
                doc.get("teacherTel"),
                doc.get("teacherPosition"),

                NullSafeParserUtil.parseLocalDateTime(doc.get("createdAt")),
                NullSafeParserUtil.parseLocalDateTime(doc.get("modifiedAt"))
        );
    }

    public static List<MemberInfoRedisModel> convertToMemberInfoRedisModel(SearchResults<String, String> results) {
        return results.stream()
                .map(MemberInfoRedisModel::convertToMemberInfoRedisModel)
                .toList();
    }

    @Override
    public String toString() {
        return "MemberInfoRedisModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", phone='" + phone + '\'' +
                ", studentId=" + studentId +
                ", studentGrade=" + studentGrade +
                ", studentRoom=" + studentRoom +
                ", studentNumber=" + studentNumber +
                ", studentCode='" + studentCode + '\'' +
                ", teacherTel='" + teacherTel + '\'' +
                ", teacherPosition='" + teacherPosition + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

}
