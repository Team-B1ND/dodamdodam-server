package b1nd.dodamcore.wakeupsong.application;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.wakeupsong.domain.entity.WakeupSong;
import b1nd.dodamcore.wakeupsong.domain.enums.WakeupSongStatus;
import b1nd.dodamcore.wakeupsong.domain.exception.WakeupSongNotFoundException;
import b1nd.dodamcore.wakeupsong.repository.WakeupSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public void delete(WakeupSong wakeupSong){
        wakeupSongRepository.delete(wakeupSong);
    }
}
