package vn.npc.ads_schedule.dto.ws;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StopCommand {

    @Builder.Default
    private final String type = "STOP";

    private Long scheduleId;
    private String reason;
}
