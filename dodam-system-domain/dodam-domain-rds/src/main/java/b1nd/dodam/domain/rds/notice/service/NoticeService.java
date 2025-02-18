package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeDivision;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.exception.NoticeDivisionNotFoundException;
import b1nd.dodam.domain.rds.notice.exception.NoticeNotFoundException;
import b1nd.dodam.domain.rds.notice.exception.UserNotAuthorException;
import b1nd.dodam.domain.rds.notice.repository.NoticeDivisionRepository;
import b1nd.dodam.domain.rds.notice.repository.NoticeFileRepository;
import b1nd.dodam.domain.rds.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final NoticeDivisionRepository noticeDivisionRepository;

    public void save(NoticeDivision noticeDivision){
        noticeDivisionRepository.save(noticeDivision);
    }

    public Notice save(Notice notice){
        noticeRepository.save(notice);
        return notice;
    }

    public NoticeDivision getNoticeDivisionById(Long id){
        return noticeDivisionRepository.findById(id)
                .orElseThrow(NoticeDivisionNotFoundException::new);
    }

    public void saveAllNoticeFiles(List<NoticeFile> noticeFiles){
        noticeFileRepository.saveAll(noticeFiles);
    }

    public void saveAll(List<NoticeDivision> noticeDivisions){
        noticeDivisionRepository.saveAll(noticeDivisions);
    }

    public void changeStatus(Long id, Member member, NoticeStatus noticeStatus) {
        Notice notice = getById(id);
        checkPermission(notice, member);
        notice.setNoticeStatus(noticeStatus);
    }

    public void updateNotice(Long id, Member member, String title, String content){
        Notice notice = getById(id);
        checkPermission(notice, member);
        if (title != null){
            notice.setTitle(title);
        }
        if (content != null){
            notice.setContent(content);
        }
    }

    private void checkPermission(Notice notice, Member member) {
        if (!notice.getMember().getId().equals(member.getId()) && !member.getRole().equals(MemberRole.ADMIN)) {
            throw new UserNotAuthorException();
        }
    }

    public Notice getById(Long id){
        return noticeRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);
    }

    public List<Notice> getAllByDivision(String memberId, Long divisionId, Long lastId, int limit){
        return noticeRepository.findNoticesByMemberAndDivision(memberId, divisionId, lastId, PageRequest.of(0, limit));
    }

    public List<Notice> getAllByStatus(String keyword, List<Long> ids, Long lastId, int limit) {
        return noticeRepository.findAllByNoticeStatus(keyword, ids, lastId, PageRequest.of(0, limit));
    }

    public List<NoticeFile> getFilesByNotices(List<Notice> notices){
        return noticeFileRepository.findAllByNoticeIn(notices);
    }

    public Map<Long, List<NoticeFile>> getNoticeFileMap(List<Notice> notices) {
        if (notices.isEmpty()) {
            return Collections.emptyMap();
        }
        return getFilesByNotices(notices)
                .stream()
                .collect(Collectors.groupingBy(noticeFile -> noticeFile.getNotice().getId()));
    }

}
