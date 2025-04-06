package b1nd.dodam.domain.redis.support;

import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.CreateOptions;
import com.redis.lettucemod.search.Field;
import io.lettuce.core.RedisCommandExecutionException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RedisIndexService implements ApplicationRunner {
    private static final String INDEX_NAME = "memberInfoIndex";
    private static final String PREFIX = "memberInfo:";

    private final RedisModulesCommands<String, String> commands;

    public RedisIndexService(@Qualifier("masterRedisearchCommands") RedisModulesCommands<String, String> commands) {
        this.commands = commands;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!isIndexExists()) {
            createMemberInfoIndex();
        } else {
            System.out.println("🔹 Redisearch 인덱스가 이미 존재합니다: " + INDEX_NAME);
        }
    }

    private boolean isIndexExists() {
        try {
            commands.ftInfo(INDEX_NAME);
            return true; // 인덱스가 존재함
        } catch (RedisCommandExecutionException e) {
            return false; // 인덱스가 없으면 예외 발생
        }
    }

    private void createMemberInfoIndex() {
        System.out.println("🛠 Redisearch 인덱스를 생성합니다: " + INDEX_NAME);

        Field<String> idField = Field.text("id").build();
        Field<String> nameField = Field.text("name").build();
        Field<String> emailField = Field.text("email").build();
        Field<String> roleField = Field.text("role").build();
        Field<String> statusField = Field.text("status").build();
        Field<String> profileImageField = Field.text("profileImage").build();
        Field<String> phoneField = Field.text("phone").build();

        Field<String> studentIdField = Field.numeric("studentId").build();
        Field<String> studentNameField = Field.text("studentName").build();
        Field<String> studentGradeField = Field.numeric("studentGrade").build();
        Field<String> studentRoomField = Field.numeric("studentRoom").build();
        Field<String> studentNumberField = Field.numeric("studentNumber").build();
        Field<String> studentCodeField = Field.text("studentCode").build();

        Field<String> teacherNameField = Field.text("teacherName").build();
        Field<String> teacherTelField = Field.text("teacherTel").build();
        Field<String> teacherPositionField = Field.text("teacherPosition").build();

        Field<String> createdAtField = Field.text("createdAt").build();
        Field<String> modifiedAtField = Field.text("modifiedAt").build();

        CreateOptions<String, String> options = CreateOptions.<String, String>builder()
                .prefix(PREFIX)
                .build();

        commands.ftCreate(INDEX_NAME, options,
                idField, nameField, emailField, roleField, statusField, profileImageField, phoneField,
                studentIdField, studentNameField, studentGradeField, studentRoomField, studentNumberField, studentCodeField,
                teacherNameField, teacherTelField, teacherPositionField,
                createdAtField, modifiedAtField
        );

        System.out.println("✅ Redisearch 인덱스 생성 완료: " + INDEX_NAME);
    }
}
