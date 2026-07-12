package vn.npc.ads_schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleVideoRequest {
    @NotNull
    private Long videoId;

    @NotNull
    @Positive
    private Integer playOrder;
}
