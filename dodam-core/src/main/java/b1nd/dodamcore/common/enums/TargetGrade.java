package b1nd.dodamcore.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum TargetGrade {

    GRADE_1("1학년"),
    GRADE_2("2학년"),
    GRADE_3("3학년"),
    GRADE_ALL("전교생"),
    GRADE_ETC("기타");

    private final String grade;

    public static TargetGrade of(String grade) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.grade, grade))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}