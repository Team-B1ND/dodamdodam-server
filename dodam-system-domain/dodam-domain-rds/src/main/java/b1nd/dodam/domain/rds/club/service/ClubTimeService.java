package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.ClubTime;
import b1nd.dodam.domain.rds.club.enumeration.ClubTimeType;
import b1nd.dodam.domain.rds.club.exception.ClubApplicationDurationPassedException;
import b1nd.dodam.domain.rds.club.repository.ClubTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClubTimeService {
    private final ClubTimeRepository clubTimeRepository;

    public ClubTime getClubTime(ClubTimeType clubTimeType) {
        return clubTimeRepository.findById(clubTimeType).orElseThrow(ClubApplicationDurationPassedException::new);
    }

    public void setClubTime(ClubTime clubTime) {
        clubTimeRepository.save(clubTime);
    }

    public void validateApplicationDuration(ClubTimeType clubTimeType) {
        ClubTime time = getClubTime(clubTimeType);
        LocalDate today = LocalDate.now();
        if (time.getStart().isBefore(today)  && time.getEnd().isAfter(today)) {
            throw new ClubApplicationDurationPassedException();
        }
    }
}
