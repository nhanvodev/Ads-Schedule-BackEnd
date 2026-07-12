package vn.npc.ads_schedule.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
public class BoxWebSocketHandler extends TextWebSocketHandler {

    private final BoxSessionRegistry sessionRegistry;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String deviceId = extractDeviceId(session);
        session.getAttributes().put("deviceId", deviceId);
        sessionRegistry.register(deviceId, session);
        log.info("Box '{}' da ket noi WebSocket (sessionId={})", deviceId, session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String deviceId = (String) session.getAttributes().get("deviceId");
        sessionRegistry.remove(deviceId);
        log.info("Box '{}' da ngat ket noi WebSocket", deviceId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Nhan tin nhan tu box: {}", message.getPayload());
    }

    private String extractDeviceId(WebSocketSession session) {
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query == null) {
            return "unknown";
        }
        return UriComponentsBuilder.newInstance()
                .query(query)
                .build()
                .getQueryParams()
                .getFirst("deviceId");
    }
}
