package b1nd.dodam.domain.rds.point.entity;

import b1nd.dodam.domain.rds.point.enumeration.ScoreType;
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
