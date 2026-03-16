package com.wit.travel.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface UploadService {

    Map<String, Object> uploadScenicImage(MultipartFile file);

    Map<String, Object> uploadAvatar(MultipartFile file);

    Map<String, Object>[] uploadScenicImages(MultipartFile[] files);
}
