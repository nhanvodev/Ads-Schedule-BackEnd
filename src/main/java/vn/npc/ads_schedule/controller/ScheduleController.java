package vn.npc.ads_schedule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.npc.ads_schedule.dto.request.CreateScheduleRequest;
import vn.npc.ads_schedule.dto.response.PlaylistItemResponse;
import vn.npc.ads_schedule.dto.response.ScheduleResponse;
import vn.npc.ads_schedule.entity.Schedule;
import vn.npc.ads_schedule.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@Valid @RequestBody CreateScheduleRequest request) {
        Schedule schedule = scheduleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ScheduleResponse.from(schedule));
    }

    @GetMapping
    public List<ScheduleResponse> getAll() {
        return scheduleService.getAll().stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ScheduleResponse getById(@PathVariable Long id) {
        return ScheduleResponse.from(scheduleService.getById(id));
    }

    @GetMapping("/{id}/playlist")
    public List<PlaylistItemResponse> getPlaylist(@PathVariable Long id) {
        return ScheduleResponse.from(scheduleService.getById(id)).getPlaylist();
    }
}
