package vn.npc.ads_schedule.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import vn.npc.ads_schedule.entity.Schedule;
import vn.npc.ads_schedule.entity.ScheduleStatus;

@Getter
@Builder
public class ScheduleResponse {
    private Long id;
    private String deviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean loopEnabled;
    private ScheduleStatus status;
    private LocalDateTime createdAt;
    private List<PlaylistItemResponse> playlist;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .deviceId(schedule.getDeviceId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .loopEnabled(schedule.getLoopEnabled())
                .status(schedule.getStatus())
                .createdAt(schedule.getCreatedAt())
                .playlist(schedule.getScheduleVideos().stream()
                        .map(PlaylistItemResponse::from)
                        .toList())
                .build();
    }
}
