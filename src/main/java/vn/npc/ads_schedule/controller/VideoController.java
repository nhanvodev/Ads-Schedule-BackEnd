package vn.npc.ads_schedule.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.npc.ads_schedule.dto.response.VideoResponse;
import vn.npc.ads_schedule.entity.Video;
import vn.npc.ads_schedule.service.VideoService;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private static final long DEFAULT_CHUNK_SIZE = 1024 * 1024; // 1MB

    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<VideoResponse> upload(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        Video video = videoService.upload(title, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(VideoResponse.from(video));
    }

    @GetMapping
    public List<VideoResponse> getAll() {
        return videoService.getAll().stream()
                .map(VideoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public VideoResponse getById(@PathVariable Long id) {
        return VideoResponse.from(videoService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        videoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> stream(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers) throws IOException {

        UrlResource video = videoService.loadAsResource(id);
        long contentLength = video.contentLength();
        ResourceRegion region;

        List<HttpRange> ranges = headers.getRange();
        if (ranges.isEmpty()) {
            long rangeLength = Math.min(DEFAULT_CHUNK_SIZE, contentLength);
            region = new ResourceRegion(video, 0, rangeLength);
        } else {
            HttpRange httpRange = ranges.get(0);
            long start = httpRange.getRangeStart(contentLength);
            long end = httpRange.getRangeEnd(contentLength);
            long rangeLength = Math.min(DEFAULT_CHUNK_SIZE, end - start + 1);
            region = new ResourceRegion(video, start, rangeLength);
        }

        MediaType mediaType = MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(mediaType)
                .body(region);
    }
}
