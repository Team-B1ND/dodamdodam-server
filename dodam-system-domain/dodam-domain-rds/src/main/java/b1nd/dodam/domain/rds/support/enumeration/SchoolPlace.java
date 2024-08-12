package b1nd.dodam.domain.rds.support.enumeration;

import b1nd.dodam.domain.rds.support.exception.SchoolPlaceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum SchoolPlace {

    PROGRAMMING_1("프로그래밍1실"),
    PROGRAMMING_2("프로그래밍2실"),
    PROGRAMMING_3("프로그래밍3실"),
    KOREAN("국어실"),
    MATH("수학실"),
    SOCIETY("사회실"),
    HALL("강당"),
    AUDIOVISUAL_ROOM("시청각실"),
    NONE("장소 없음"),
    ETC("기타");

    private final String place;

    public static SchoolPlace of(String place) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.place, place))
                .findAny()
                .orElseThrow(SchoolPlaceNotFoundException::new);
    }

}
