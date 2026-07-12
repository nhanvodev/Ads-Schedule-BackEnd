package vn.npc.ads_schedule.dto.ws;

import lombok.Builder;
import lombok.Getter;
import vn.npc.ads_schedule.dto.response.PlaylistItemResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlayCommand {

    @Builder.Default
    private final String type = "PLAY";

    private Long scheduleId;
    private LocalDateTime endTime;
    private Boolean loop;
    private List<PlaylistItemResponse> playlist;
}
