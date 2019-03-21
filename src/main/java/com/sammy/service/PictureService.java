package com.sammy.service;

import com.sammy.entity.api.PictureApiDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {
    Resource findOneImage(String filename);
    Page<PictureApiDTO> findPage(Pageable pageable);
    void createPicture(MultipartFile file) throws IOException;
    void deletePicture(String filename) throws IOException;
}