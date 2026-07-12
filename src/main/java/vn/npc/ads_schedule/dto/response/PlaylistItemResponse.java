package vn.npc.ads_schedule.dto.response;

import lombok.Builder;
import lombok.Getter;
import vn.npc.ads_schedule.entity.ScheduleVideo;

@Getter
@Builder
public class PlaylistItemResponse {
    private Long videoId;
    private String title;
    private Integer playOrder;
    private String streamUrl;

    public static PlaylistItemResponse from(ScheduleVideo scheduleVideo) {
        return PlaylistItemResponse.builder()
                .videoId(scheduleVideo.getVideo().getId())
                .title(scheduleVideo.getVideo().getTitle())
                .playOrder(scheduleVideo.getPlayOrder())
                .streamUrl("/api/videos/" + scheduleVideo.getVideo().getId() + "/stream")
                .build();
    }
}
