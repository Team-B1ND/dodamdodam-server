package b1nd.dodam.domain.rds.notice.service;

import b1nd.dodam.domain.rds.notice.entity.Notice;
import b1nd.dodam.domain.rds.notice.enumration.NoticeStatus;
import b1nd.dodam.domain.rds.notice.exception.NoticeNotFoundException;
import b1nd.dodam.domain.rds.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
