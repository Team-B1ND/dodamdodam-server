package b1nd.dodamcore.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum SchoolPlace {

    programming_1("프로그래밍1실");

    private final String place;

    public static SchoolPlace of(String place) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.place, place))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}