package com.wit.travel.service.impl;

import com.wit.travel.service.UploadService;
import com.wit.travel.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Value("${file.upload.path:uploads}")
    private String uploadBasePath;  // 注入配置值

    @Override
    public Map<String, Object> uploadScenicImage(MultipartFile file) {
        try {
            return fileUploadUtil.uploadScenicImage(file);
        } catch (Exception e) {
            throw new RuntimeException("上传失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> uploadAvatar(MultipartFile file) {
        try {
            return fileUploadUtil.uploadAvatar(file);
        } catch (Exception e) {
            throw new RuntimeException("上传失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object>[] uploadScenicImages(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new RuntimeException("文件不能为空");
        }

        if (files.length > 9) {
            throw new RuntimeException("最多支持上传9张图片");
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Map<String, Object> result = fileUploadUtil.uploadScenicImage(file);
                results.add(result);
            } catch (Exception e) {
                throw new RuntimeException("上传失败：" + e.getMessage());
            }
        }

        return results.toArray(new Map[0]);
    }

    @PostConstruct
    public void init() {
        System.out.println("上传路径配置为：" + uploadBasePath);
    }
}
