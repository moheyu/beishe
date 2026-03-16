package com.wit.travel.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.upload.domain:http://localhost:8080/api}")
    private String domain;

    private static final String[] SCENIC_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif"};
    private static final String[] AVATAR_TYPES = {"jpg", "jpeg", "png"};
    private static final long SCENIC_MAX_SIZE = 5 * 1024 * 1024;
    private static final long AVATAR_MAX_SIZE = 2 * 1024 * 1024;

    public Map<String, Object> uploadScenicImage(MultipartFile file) throws IOException {
        validateFile(file, SCENIC_IMAGE_TYPES, SCENIC_MAX_SIZE);
        String filePath = saveFile(file, "scenic");
        return buildUploadResult(file, filePath);
    }

    public Map<String, Object> uploadAvatar(MultipartFile file) throws IOException {
        validateFile(file, AVATAR_TYPES, AVATAR_MAX_SIZE);
        String filePath = saveFile(file, "avatar");
        return buildUploadResult(file, filePath);
    }

    private void validateFile(MultipartFile file, String[] allowedTypes, long maxSize) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名不能为空");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean isValidType = false;
        for (String type : allowedTypes) {
            if (type.equals(extension)) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            throw new RuntimeException("文件格式不支持");
        }

        if (file.getSize() > maxSize) {
            throw new RuntimeException("文件大小超过限制");
        }
    }

    private String saveFile(MultipartFile file, String category) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String newFileName = UUID.randomUUID().toString() + "." + extension;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String datePath = sdf.format(new Date());

        String relativePath = category + File.separator + datePath;
        String absolutePath = uploadPath + File.separator + relativePath;

        File dir = new File(absolutePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File destFile = new File(absolutePath + File.separator + newFileName);
        file.transferTo(destFile);

        return relativePath + File.separator + newFileName;
    }

    private Map<String, Object> buildUploadResult(MultipartFile file, String filePath) {
        Map<String, Object> result = new HashMap<>();
        result.put("url", "/uploads/" + filePath.replace("\\", "/"));
        result.put("fileName", new File(filePath).getName());
        result.put("fileSize", file.getSize());
        return result;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }
}
