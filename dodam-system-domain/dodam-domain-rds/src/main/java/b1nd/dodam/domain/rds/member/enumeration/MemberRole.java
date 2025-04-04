package b1nd.dodam.domain.rds.member.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    STUDENT("ROLE_STUDENT", 1),
    PARENT("ROLE_PARENT", 2),
    TEACHER("ROLE_TEACHER", 3),
    ADMIN("ROLE_ADMIN", 4);

    private final String role;
    private final int number;

    public static MemberRole of(int number) {
        return Arrays.stream(values())
                .filter(value -> value.number == number)
                .findAny()
                .orElse(null);
    }

    public static MemberRole of(String role) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.role, role))
                .findAny()
                .orElse(null);
    }

}
