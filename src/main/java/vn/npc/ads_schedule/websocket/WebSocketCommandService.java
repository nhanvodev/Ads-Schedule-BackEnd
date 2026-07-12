package vn.npc.ads_schedule.websocket;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import vn.npc.ads_schedule.dto.response.PlaylistItemResponse;
import vn.npc.ads_schedule.dto.ws.PlayCommand;
import vn.npc.ads_schedule.dto.ws.StopCommand;
import vn.npc.ads_schedule.entity.Schedule;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketCommandService {

    private final BoxSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper;

    public void sendPlay(Schedule schedule) {
        List<PlaylistItemResponse> playlist = schedule.getScheduleVideos().stream()
                .map(PlaylistItemResponse::from)
                .toList();

        PlayCommand command = PlayCommand.builder()
                .scheduleId(schedule.getId())
                .endTime(schedule.getEndTime())
                .loop(schedule.getLoopEnabled())
                .playlist(playlist)
                .build();

        send(schedule.getDeviceId(), command);
    }

    public void sendStop(Schedule schedule, String reason) {
        StopCommand command = StopCommand.builder()
                .scheduleId(schedule.getId())
                .reason(reason)
                .build();

        send(schedule.getDeviceId(), command);
    }

    private void send(String deviceId, Object command) {
        WebSocketSession session = sessionRegistry.get(deviceId);
        if (session == null || !session.isOpen()) {
            log.warn("Box '{}' chua ket noi WebSocket, khong the ban lenh", deviceId);
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(command);
            session.sendMessage(new TextMessage(json));
            log.info("Da ban lenh toi box '{}': {}", deviceId, json);
        } catch (IOException e) {
            log.error("Loi khi gui lenh WebSocket toi box '{}'", deviceId, e);
        }
    }
}
