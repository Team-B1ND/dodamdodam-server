package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.entity.NoticeFile;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.exception.NoticeNotFoundException;
import b1nd.dodam.domain.rds.notice.repository.NoticeFileRepository;
import b1nd.dodam.domain.rds.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeFileRepository noticeFileRepository;

    public Notice save(Notice notice){
        noticeRepository.save(notice);
        return notice;
    }

    public void changeStatus(Long id, NoticeStatus noticeStatus){
        Notice notice = getById(id);
        notice.setNoticeStatus(noticeStatus);
    }

    public Notice getById(Long id){
        return noticeRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);
    }

    public List<Notice> getAllByDivision(String memberId, Long divisionId, Long lastId, int limit){
        return noticeRepository.findNoticesByMemberAndDivision(memberId, divisionId, lastId, PageRequest.of(0, limit));
    }

    public List<Notice> getAllByStatus(String keyword, List<Long> ids, NoticeStatus noticeStatus, Long lastId, int limit) {
        // 페이지 번호를 계산합니다. lastId가 있다면, 해당 id를 기준으로 다음 페이지를 요청합니다.
        int page = (lastId == null) ? 0 : (int) (lastId / limit);

        return noticeRepository.findAllByNoticeStatus(keyword, ids, noticeStatus, lastId, PageRequest.of(page, limit));
    }

    public List<NoticeFile> getAllByNotice(List<Notice> notices){
        return noticeFileRepository.findAllByNoticeIn(notices);
    }


}
