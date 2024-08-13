package b1nd.dodam.domain.rds.wakeupsong.repository;

import b1nd.dodam.domain.rds.wakeupsong.entity.WakeupSong;
import b1nd.dodam.domain.rds.wakeupsong.enumeration.WakeupSongStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WakeupSongRepository extends JpaRepository<WakeupSong, Integer> {

    @EntityGraph(attributePaths = {"member"})
    Optional<WakeupSong> findById(int id);

    List<WakeupSong> findAllByPlayAt(LocalDate playDate);

    List<WakeupSong> findAllByStatus(WakeupSongStatus status);

    List<WakeupSong> findAllByMember_IdAndStatus(String memberId, WakeupSongStatus status);

    boolean existsByMember_IdAndCreatedAtAfter(String memberId, LocalDateTime createdAt);

}
