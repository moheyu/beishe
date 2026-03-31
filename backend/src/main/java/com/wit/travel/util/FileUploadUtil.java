package com.wit.travel.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传工具类，处理景点图片和用户头像的本地存储。
 */
@Slf4j
@Component
public class FileUploadUtil {

    private static final Set<String> SCENIC_IMAGE_TYPES = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png", "gif"));
    private static final Set<String> AVATAR_TYPES = new HashSet<>(
            Arrays.asList("jpg", "jpeg", "png"));
    private static final long SCENIC_MAX_SIZE = 5 * 1024 * 1024L;
    private static final long AVATAR_MAX_SIZE = 2 * 1024 * 1024L;

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

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

    private void validateFile(MultipartFile file, Set<String> allowedTypes, long maxSize) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!allowedTypes.contains(extension)) {
            throw new RuntimeException("文件格式不支持，仅允许：" + allowedTypes);
        }
        if (file.getSize() > maxSize) {
            throw new RuntimeException("文件大小超过限制（最大 " + (maxSize / 1024 / 1024) + "MB）");
        }
    }

    private String saveFile(MultipartFile file, String category) throws IOException {
        String extension = getFileExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String relativePath = category + File.separator + datePath;
        File dir = new File(uploadPath + File.separator + relativePath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("创建上传目录失败：" + dir.getAbsolutePath());
        }

        File destFile = new File(dir, newFileName);
        file.transferTo(destFile);
        return relativePath + File.separator + newFileName;
    }

    private Map<String, Object> buildUploadResult(MultipartFile file, String filePath) {
        Map<String, Object> result = new HashMap<>(4);
        result.put("url", "/uploads/" + filePath.replace("\\", "/"));
        result.put("fileName", new File(filePath).getName());
        result.put("fileSize", file.getSize());
        return result;
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex > 0 && dotIndex < filename.length() - 1)
                ? filename.substring(dotIndex + 1)
                : "";
    }

    /**
     * 删除单个文件。
     *
     * @param imageUrl 图片 URL，格式如 /uploads/scenic/20250318/xxx.jpg
     */
    public void deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return;
        }
        if (!imageUrl.startsWith("/uploads/")) {
            return;
        }
        String relativePath = imageUrl.substring("/uploads/".length()).replace("/", File.separator);
        File file = new File(uploadPath + File.separator + relativePath);
        if (file.exists()) {
            if (file.delete()) {
                log.info("文件删除成功：{}", file.getAbsolutePath());
            } else {
                log.warn("文件删除失败：{}", file.getAbsolutePath());
            }
        } else {
            log.debug("文件不存在，跳过删除：{}", file.getAbsolutePath());
        }
    }

    /**
     * 批量删除文件。
     *
     * @param imageUrls 图片 URL 数组
     */
    public void deleteFiles(String[] imageUrls) {
        if (imageUrls == null || imageUrls.length == 0) {
            return;
        }
        for (String imageUrl : imageUrls) {
            deleteFile(imageUrl);
        }
    }
}
