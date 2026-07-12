package vn.npc.ads_schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.npc.ads_schedule.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long>{
    
}
