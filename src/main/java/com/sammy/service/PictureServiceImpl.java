package com.sammy.service;

import com.sammy.entity.api.PictureApiDTO;
import com.sammy.entity.business.PictureBusinessDTO;
import com.sammy.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.sammy.mapper.PictureMapper.toApi;
import static com.sammy.mapper.PictureMapper.toApiPageable;
import static com.sammy.mapper.PictureMapper.toBusiness;

@Service
public class PictureServiceImpl implements PictureService {

    private static String UPLOAD_ROOT = "upload-dir";

    private final PictureRepository repository;
    private final ResourceLoader resourceLoader;
    private final CounterService counterService;
    private final GaugeService gaugeService;
    private final InMemoryMetricRepository metricRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PictureServiceImpl(PictureRepository repository, ResourceLoader resourceLoader, CounterService counterService,
                              GaugeService gaugeService, InMemoryMetricRepository metricRepository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
        this.metricRepository = metricRepository;
        this.messagingTemplate = messagingTemplate;

        this.counterService.reset("files.uploaded");
        this.gaugeService.submit("files.uploaded.lastBytes", 0);
        this.metricRepository.set(new Delta<Number>("files.uploaded.totalBytes", 0));
    }

    @Override
    public Resource findOneImage(String filename){
        return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename);
    }

    @Override
    public Page<PictureApiDTO> findPage(Pageable pageable) {
        return toApiPageable(repository.findAll(pageable));
    }

    @Override
    public void createPicture(MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
            repository.save(PictureBusinessDTO.builder()
                                              .name(file.getOriginalFilename())
                                              .build());
            counterService.increment("files.uploaded");
            gaugeService.submit("files.uploaded.lastBytes", file.getSize());
            metricRepository.increment(new Delta<Number>("files.uploaded.totalBytes", file.getSize()));
            messagingTemplate.convertAndSend("/topic/newPicture", file.getOriginalFilename());
        }
    }

    @Override
    public void deletePicture(String filename) throws IOException {
        final PictureBusinessDTO byName = repository.findByName(filename);
        repository.delete(byName);
        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
        messagingTemplate.convertAndSend("/topic/deletePicture", filename);
    }
}