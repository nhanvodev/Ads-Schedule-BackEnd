package vn.npc.ads_schedule.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import vn.npc.ads_schedule.entity.Video;

@Getter
@Builder
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String title;
    private Long fileSize;
    private Integer durationSec;
    private LocalDateTime createdAt;
    private String streamUrl;

    public static VideoResponse from(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .fileSize(video.getFileSize())
                .durationSec(video.getDurationSec())
                .createdAt(video.getCreatedAt())
                .streamUrl("/api/videos/" + video.getId() + "/stream")
                .build();
    }
}
