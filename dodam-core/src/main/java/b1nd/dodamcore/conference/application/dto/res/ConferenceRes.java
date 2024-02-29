package b1nd.dodamcore.conference.application.dto.res;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ConferenceRes(
        String title,
        String organization,
        LocalDate startDate,
        LocalDate endDate,
        String eventType,
        String link
) {}