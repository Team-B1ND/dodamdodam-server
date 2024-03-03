package b1nd.dodamcore.wakeupsong.repository;

import b1nd.dodamcore.wakeupsong.domain.entity.WakeupSong;
import b1nd.dodamcore.wakeupsong.domain.enums.WakeupSongStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WakeupSongRepository extends JpaRepository<WakeupSong, Integer> {

    @EntityGraph(attributePaths = {"member"})
    Optional<WakeupSong> findById(int id);

    List<WakeupSong> findAllByPlayAt(LocalDate playDate);

    List<WakeupSong> findAllByStatus(WakeupSongStatus status);

    List<WakeupSong> findAllByMember_IdAndStatus(String memberId, WakeupSongStatus status);

    boolean existsByMember_IdAndCreatedAtAfter(String memberId, LocalDateTime createdAt);
}
