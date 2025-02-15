package b1nd.dodam.domain.rds.support.enumeration;

import b1nd.dodam.domain.rds.support.exception.SchoolPlaceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum SchoolPlace {

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
