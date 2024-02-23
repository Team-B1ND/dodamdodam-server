package b1nd.dodamcore.point.domain.entity;

import b1nd.dodamcore.point.domain.enums.ScoreType;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Score {

    private int bonus;

    private int minus;

    private int offset;

    public void issue(ScoreType type, int score) {
        switch (type) {
            case BONUS -> bonus += score;
            case MINUS -> minus += score;
            case OFFSET -> offset += score;
        }
    }

    public void cancel(ScoreType type, int score) {
        switch (type) {
            case BONUS -> bonus -= score;
            case MINUS -> minus -= score;
            case OFFSET -> offset -= score;
        }
    }

}