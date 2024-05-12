package b1nd.dodamcore.common.enums;

import b1nd.dodamcore.common.exception.ExceptionCode;
import b1nd.dodamcore.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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
                .orElseThrow(PlaceNotFoundException::new);
    }

}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class PlaceNotFoundException extends CustomException {

    public PlaceNotFoundException() {
        super(PlaceExceptionCode.NOT_FOUND);
    }

}

@RequiredArgsConstructor
enum PlaceExceptionCode implements ExceptionCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 장소");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getExceptionName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
