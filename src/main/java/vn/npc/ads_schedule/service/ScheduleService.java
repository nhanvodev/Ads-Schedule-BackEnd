package vn.npc.ads_schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.npc.ads_schedule.dto.request.CreateScheduleRequest;
import vn.npc.ads_schedule.dto.request.ScheduleVideoRequest;
import vn.npc.ads_schedule.entity.Schedule;
import vn.npc.ads_schedule.entity.ScheduleStatus;
import vn.npc.ads_schedule.entity.ScheduleVideo;
import vn.npc.ads_schedule.entity.Video;
import vn.npc.ads_schedule.exception.ResourceNotFoundException;
import vn.npc.ads_schedule.exception.ScheduleOverlapException;
import vn.npc.ads_schedule.repository.ScheduleRepository;
import vn.npc.ads_schedule.repository.VideoRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private static final long NO_EXCLUDE = 0L;

    private final ScheduleRepository scheduleRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public Schedule create(CreateScheduleRequest request) {
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException("startTime phai truoc endTime");
        }

        long distinctOrders = request.getVideos().stream()
                .map(ScheduleVideoRequest::getPlayOrder)
                .distinct()
                .count();
        if (distinctOrders != request.getVideos().size()) {
            throw new IllegalArgumentException("play_order bi trung trong danh sach video");
        }

        List<Schedule> overlaps = scheduleRepository.findOverlapping(
                request.getDeviceId(), request.getStartTime(), request.getEndTime(), NO_EXCLUDE);
        if (!overlaps.isEmpty()) {
            throw new ScheduleOverlapException(
                    "Khung gio bi trung voi lich id=" + overlaps.get(0).getId()
                            + " tren device " + request.getDeviceId());
        }

        Schedule schedule = Schedule.builder()
                .deviceId(request.getDeviceId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .loopEnabled(request.getLoopEnabled() != null ? request.getLoopEnabled() : true)
                .status(ScheduleStatus.PENDING)
                .build();

        for (ScheduleVideoRequest item : request.getVideos()) {
            Video video = videoRepository.findById(item.getVideoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay video id=" + item.getVideoId()));

            schedule.getScheduleVideos().add(ScheduleVideo.builder()
                    .schedule(schedule)
                    .video(video)
                    .playOrder(item.getPlayOrder())
                    .build());
        }

        return scheduleRepository.save(schedule);
    }

    public Schedule getById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay lich id=" + id));
    }

    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }
}
