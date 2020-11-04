package com.soubao.service.impl;

import com.soubao.common.constant.ShopConstant;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.ConfigService;
import com.soubao.service.UploadFileService;
import com.soubao.common.utils.OSSClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;


@Service("uploadFileService")
public class UploadFileServiceImpl implements UploadFileService {
    @Autowired
    private ShopConstant shopConstant;
    @Value("${spring.resources.static-locations}")
    private String staticLocation;
    @Autowired
    private ConfigService configService;


    @Override
    public String uploadImage(MultipartFile file, String type) {
        //文件大小限制
        Long imageMaxSize = shopConstant.getImageMaxSize();
        if (file.getSize() > imageMaxSize) {
            throw new ShopException(-19000, "上传图片文件不能超过" + imageMaxSize / 1024 / 1024 + "M");
        }
        //图片类型限制
        String contentType = file.getContentType();
        if (!shopConstant.getImageType().contains(contentType)) {
            throw new ShopException(ResultEnum.IMAGE_TYPE_ERROR);
        }

        //  ../public/upload
        String uploadPath = staticLocation.substring(staticLocation.indexOf(":") + 1) + shopConstant.getImageUpload();
        //文件保存路径
        StringBuilder filePath = new StringBuilder(uploadPath);
        //保存到对应文件夹下对应当天日期文件夹
        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        String monthWithDay = Calendar.getInstance().get(Calendar.MONTH) + 1 + "-" + Calendar.getInstance().get(Calendar.DATE);
        filePath.append("/").append(type).append("/").append(year).append("/").append(monthWithDay).append("/");

        //阿里云OSS 对象存储
        Map<Object, Object> configMap = configService.getConfigMap();
        if (configMap != null) {
            Object ossSwitchObj = configMap.get("oss_oss_switch");
            if (ossSwitchObj != null) {
                if ("1".equals(ossSwitchObj.toString())) {//启用了对象存储
                    String OSSFilePath = "oss/" + filePath.toString().substring(filePath.indexOf("/") + 1);
                    OSSClientUtil ossClientUtil = new OSSClientUtil(
                            configMap.get("oss_oss_key_id").toString(),
                            configMap.get("oss_oss_key_secret").toString(),
                            configMap.get("oss_oss_endpoint").toString(),
                            configMap.get("oss_oss_bucket").toString(),
                            OSSFilePath);
                    //上传并返回图片路径，file为MultipartFile类型
                    return ossClientUtil.uploadImage(file);
                }
            }
        }

        //未开启OSS
        //随机文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        String randomFileName = new Random().nextInt(10000) + System.currentTimeMillis() + fileSuffix;
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath.append(randomFileName).toString());
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, bytes);
            String pathStr = path.toString().replace("\\", "/");
            //数据库保存路径
            return pathStr.substring(pathStr.indexOf(uploadPath.substring(uploadPath.indexOf("/"))));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ShopException(ResultEnum.UPLOAD_FAIL);
        }
    }

    @Override
    public String uploadImageWx(MultipartFile file) {
        //文件大小限制
        if (file.getSize() > 1048576) {
            throw new ShopException(ResultEnum.UPLOAD_WX_IMG_MAX_SIZE_ERROR);
        }
        //图片类型限制 jpg/png
        String contentType = file.getContentType();
        if (!"image/jpg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new ShopException(ResultEnum.IMAGE_TYPE_ERROR);
        }

        //  ../public/upload
        String uploadPath = staticLocation.substring(staticLocation.indexOf(":") + 1) + shopConstant.getImageUpload();
        //文件保存路径
        StringBuilder filePath = new StringBuilder(uploadPath);
        //保存到对应文件夹下对应当天日期文件夹
        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        String monthWithDay = Calendar.getInstance().get(Calendar.MONTH) + 1 + "-" + Calendar.getInstance().get(Calendar.DATE);
        filePath.append("/").append("wx_uploadimg").append("/").append(year).append("/").append(monthWithDay).append("/");

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        String randomFileName = new Random().nextInt(10000) + System.currentTimeMillis() + fileSuffix;
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath.append(randomFileName).toString());
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, bytes);
            String pathStr = path.toString().replace("\\", "/");
            return pathStr;
            //数据库保存路径
            //return pathStr.substring(pathStr.indexOf(uploadPath.substring(uploadPath.indexOf("/"))));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ShopException(ResultEnum.UPLOAD_FAIL);
        }
    }

}




















