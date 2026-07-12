package vn.npc.ads_schedule.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.npc.ads_schedule.entity.Schedule;
import vn.npc.ads_schedule.entity.ScheduleStatus;
import vn.npc.ads_schedule.repository.ScheduleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleSchedulerService {
    private final ScheduleRepository scheduleRepository;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void syncScheduleStatuses() {
        LocalDateTime now = LocalDateTime.now();

        List<Schedule> toStart = scheduleRepository.findByStatusAndStartTimeLessThanEqualAndEndTimeAfter(
                ScheduleStatus.PENDING, now, now);
        toStart.forEach(schedule -> {
            schedule.setStatus(ScheduleStatus.PLAYING);
            log.info("Schedule {} (device={}) chuyen PENDING -> PLAYING", schedule.getId(), schedule.getDeviceId());
            // TODO Buoc 5: ban lenh PLAY qua WebSocket cho schedule nay
        });

        List<Schedule> missed = scheduleRepository.findByStatusAndEndTimeLessThanEqual(
                ScheduleStatus.PENDING, now);
        missed.forEach(schedule -> {
            schedule.setStatus(ScheduleStatus.FINISHED);
            log.warn("Schedule {} (device={}) da qua gio ma chua chay, bo qua va chuyen thang sang FINISHED",
                    schedule.getId(), schedule.getDeviceId());
        });

        List<Schedule> toFinish = scheduleRepository.findByStatusAndEndTimeLessThanEqual(
                ScheduleStatus.PLAYING, now);
        toFinish.forEach(schedule -> {
            schedule.setStatus(ScheduleStatus.FINISHED);
            log.info("Schedule {} (device={}) chuyen PLAYING -> FINISHED", schedule.getId(), schedule.getDeviceId());
            // TODO Buoc 5: ban lenh STOP qua WebSocket cho schedule nay
        });

        scheduleRepository.saveAll(toStart);
        scheduleRepository.saveAll(missed);
        scheduleRepository.saveAll(toFinish);
    }
}
