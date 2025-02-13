package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.exception.NoticeNotFoundException;
import b1nd.dodam.domain.rds.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

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

    public List<Notice> getAllByStatus(String keyword, List<Long> ids, NoticeStatus noticeStatus, Long lastId, int limit){
        return noticeRepository.findAllByNoticeStatus(keyword, ids, noticeStatus, lastId, PageRequest.of(0, limit));
    }

}
