package vn.npc.ads_schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.npc.ads_schedule.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
            SELECT s FROM Schedule s
            WHERE s.deviceId = :deviceId
              AND s.id <> :excludeId
              AND s.startTime < :endTime
              AND s.endTime > :startTime
            """)
    List<Schedule> findOverlapping(
            @Param("deviceId") String deviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId);
}
