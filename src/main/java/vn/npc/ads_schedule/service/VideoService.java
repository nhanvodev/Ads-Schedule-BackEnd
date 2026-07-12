package vn.npc.ads_schedule.service;

import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.npc.ads_schedule.entity.Video;
import vn.npc.ads_schedule.exception.ResourceNotFoundException;
import vn.npc.ads_schedule.repository.VideoRepository;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public Video upload(String title, MultipartFile file) {
        String filePath = fileStorageService.store(file);

        Video video = Video.builder()
                .title(title)
                .filePath(filePath)
                .fileSize(file.getSize())
                .build();

        return videoRepository.save(video);
    }

    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    public Video getById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay video id=" + id));
    }

    @Transactional
    public void delete(Long id) {
        Video video = getById(id);
        fileStorageService.delete(video.getFilePath());
        videoRepository.delete(video);
    }

    public UrlResource loadAsResource(Long id) {
        Video video = getById(id);
        try {
            return new UrlResource("file:" + video.getFilePath());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Duong dan file khong hop le: " + video.getFilePath(), e);
        }
    }
}
