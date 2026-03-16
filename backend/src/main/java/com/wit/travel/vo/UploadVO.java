package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传返回VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVO {

    private String url;

    private String fileName;

    private Long fileSize;
}
