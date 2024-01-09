package b1nd.dodamcore.member.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    STUDENT("ROLE_STUDENT", 1),
    PARENT("ROLE_PARENT", 2),
    TEACHER("ROLE_TEACHER", 3),
    ADMIN("ROLE_ADMIN", 4);

    private final String role;
    private final int number;

    public static MemberRole valueOfNumber(int number) {
        return Arrays.stream(values())
                .filter(value -> value.number == number)
                .findAny()
                .orElse(null);
    }

}
