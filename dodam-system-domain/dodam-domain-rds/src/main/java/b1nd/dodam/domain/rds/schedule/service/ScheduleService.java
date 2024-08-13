package b1nd.dodam.domain.rds.schedule.service;

import b1nd.dodam.domain.rds.schedule.entity.Schedule;
import b1nd.dodam.domain.rds.schedule.exception.ScheduleNotFoundException;
import b1nd.dodam.domain.rds.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository repository;

    public void save(Schedule schedule) {
        repository.save(schedule);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public Schedule getById(int id) {
        return repository.findById(id)
                .orElseThrow(ScheduleNotFoundException::new);
    }

    public List<Schedule> getByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<Schedule> getByPeriod(LocalDate startAt, LocalDate endAt) {
        return repository.findByDateBetween(startAt, endAt);
    }

    public List<Schedule> searchByName(String name) {
        return repository.findByNameContaining(name);
    }

    public List<Schedule> getAllOrderByIdDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return repository.findAllByOrderByIdDesc(pageable);
    }

}
