package b1nd.dodam.domain.rds.wakeupsong.service;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.wakeupsong.entity.WakeupSong;
import b1nd.dodam.domain.rds.wakeupsong.enumeration.WakeupSongStatus;
import b1nd.dodam.domain.rds.wakeupsong.exception.WakeupSongNotFoundException;
import b1nd.dodam.domain.rds.wakeupsong.repository.WakeupSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WakeupSongService {

    private final WakeupSongRepository wakeupSongRepository;

    public List<WakeupSong> getByPlayDate(int year, int month, int day) {
        return wakeupSongRepository.findAllByPlayAt(LocalDate.of(year, month, day));
    }

    public List<WakeupSong> getPendingWakeupSong() {
        return wakeupSongRepository.findAllByStatus(WakeupSongStatus.PENDING);
    }

    public List<WakeupSong> getMyWakeupSong(Member member) {
        return wakeupSongRepository.findAllByMember_IdAndStatus(member.getId(), WakeupSongStatus.PENDING);
    }

    public void saveWakeupSong(WakeupSong wakeupSong){
        wakeupSongRepository.save(wakeupSong);
    }

    public Boolean existsByMemberAndCreatedAt(Member member){
        return wakeupSongRepository.existsByMember_IdAndCreatedAtAfter(member.getId(), ZonedDateTimeUtil.nowToLocalDateTime().minusDays(7));
    }

    public WakeupSong getById(int id){
        return wakeupSongRepository.findById(id)
                .orElseThrow(WakeupSongNotFoundException::new);
    }

    public void delete(WakeupSong wakeupSong){
        wakeupSongRepository.delete(wakeupSong);
    }

}
