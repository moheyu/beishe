package com.wit.travel.service.impl;

import com.wit.travel.service.UploadService;
import com.wit.travel.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传 Service 实现
 */
@Service
public class UploadServiceImpl implements UploadService {

    private static final int MAX_BATCH_UPLOAD = 9;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Override
    public Map<String, Object> uploadScenicImage(MultipartFile file) {
        try {
            return fileUploadUtil.uploadScenicImage(file);
        } catch (Exception e) {
            throw new RuntimeException("景点图片上传失败：" + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> uploadAvatar(MultipartFile file) {
        try {
            return fileUploadUtil.uploadAvatar(file);
        } catch (Exception e) {
            throw new RuntimeException("头像上传失败：" + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object>[] uploadScenicImages(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new RuntimeException("文件不能为空");
        }
        if (files.length > MAX_BATCH_UPLOAD) {
            throw new RuntimeException("最多支持批量上传 " + MAX_BATCH_UPLOAD + " 张图片");
        }
        List<Map<String, Object>> results = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            try {
                results.add(fileUploadUtil.uploadScenicImage(file));
            } catch (Exception e) {
                throw new RuntimeException("文件 [" + file.getOriginalFilename() + "] 上传失败：" + e.getMessage(), e);
            }
        }
        return results.toArray(new Map[0]);
    }
}
