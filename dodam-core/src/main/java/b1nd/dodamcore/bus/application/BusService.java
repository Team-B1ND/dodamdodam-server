package b1nd.dodamcore.bus.application;

import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.exception.BusFullOfSeatException;
import b1nd.dodamcore.bus.domain.exception.BusNotFoundException;
import b1nd.dodamcore.bus.domain.exception.BusPeriodExpiredException;
import b1nd.dodamcore.bus.repository.BusRepository;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository repository;

    public void save(Bus bus) {
        repository.save(bus);
    }

    public void delete(Bus bus) {
        repository.delete(bus);
    }

    public Bus getById(int id) {
        return repository.findById(id)
                .orElseThrow(BusNotFoundException::new);
    }

    public Bus getByIdForUpdate(int id) {
        Bus bus = repository.findByIdWithPessimisticLock(id)
                .orElseThrow(BusNotFoundException::new);
        if(bus.isFullOfSeat()) {
            throw new BusFullOfSeatException();
        }
        if(bus.isExpired(ZonedDateTimeUtil.nowToLocalDateTime())) {
            throw new BusPeriodExpiredException();
        }
        return bus;
    }

    public Bus getByStudent(LocalDateTime now, int studentId) {
        return repository.findBusByStudent(now, studentId);
    }

    public List<Bus> getAll(int page, int limit) {
        return repository.findAllByOrderByIdDesc(PageRequest.of(page - 1, limit));
    }

    public List<Bus> getAllByDate(LocalDate date) {
        return repository.findAllByLeaveTime(date);
    }

    public List<Bus> getValid(LocalDateTime startAt, LocalDateTime endAt) {
        return repository.findBusByLeaveTimeBetween(startAt, endAt);
    }

}
