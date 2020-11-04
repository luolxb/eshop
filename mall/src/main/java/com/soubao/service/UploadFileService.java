package com.soubao.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    //上传图片
    String uploadImage(MultipartFile file, String type);

    /**
     * 上传微信公众号图文消息内的图片素材
     * @param file 上传的图片文件
     * @return 文件路径
     */
    String uploadImageWx(MultipartFile file);
}
