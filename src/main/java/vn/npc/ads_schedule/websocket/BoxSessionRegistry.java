package vn.npc.ads_schedule.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BoxSessionRegistry {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void register(String deviceId, WebSocketSession session) {
        sessions.put(deviceId, session);
    }

    public void remove(String deviceId) {
        sessions.remove(deviceId);
    }

    public WebSocketSession get(String deviceId) {
        return sessions.get(deviceId);
    }
}
