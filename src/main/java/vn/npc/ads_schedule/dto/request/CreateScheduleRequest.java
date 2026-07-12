package vn.npc.ads_schedule.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateScheduleRequest {
    private String deviceId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private Boolean loopEnabled;

    @NotEmpty(message = "Danh sach video khong duoc rong")
    @Valid
    private List<ScheduleVideoRequest> videos;
}
