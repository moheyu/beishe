package com.wit.travel.controller;

import com.wit.travel.service.UploadService;
import com.wit.travel.vo.Result;
import com.wit.travel.vo.UploadVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/scenic-image")
    public Result<UploadVO> uploadScenicImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> uploadResult = uploadService.uploadScenicImage(file);
        
        UploadVO uploadVO = new UploadVO();
        uploadVO.setUrl((String) uploadResult.get("url"));
        uploadVO.setFileName((String) uploadResult.get("fileName"));
        uploadVO.setFileSize((Long) uploadResult.get("fileSize"));
        
        return Result.success(uploadVO);
    }

    @PostMapping("/avatar")
    public Result<UploadVO> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, Object> uploadResult = uploadService.uploadAvatar(file);
        
        UploadVO uploadVO = new UploadVO();
        uploadVO.setUrl((String) uploadResult.get("url"));
        uploadVO.setFileName((String) uploadResult.get("fileName"));
        uploadVO.setFileSize((Long) uploadResult.get("fileSize"));
        
        return Result.success(uploadVO);
    }

    @PostMapping("/scenic-images")
    public Result<UploadVO[]> uploadScenicImages(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object>[] uploadResults = uploadService.uploadScenicImages(files);
        
        UploadVO[] uploadVOs = new UploadVO[uploadResults.length];
        for (int i = 0; i < uploadResults.length; i++) {
            UploadVO uploadVO = new UploadVO();
            uploadVO.setUrl((String) uploadResults[i].get("url"));
            uploadVO.setFileName((String) uploadResults[i].get("fileName"));
            uploadVO.setFileSize((Long) uploadResults[i].get("fileSize"));
            uploadVOs[i] = uploadVO;
        }
        
        return Result.success(uploadVOs);
    }
}
