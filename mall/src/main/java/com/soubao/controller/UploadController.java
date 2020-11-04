package com.soubao.controller;

import com.alibaba.fastjson.JSONObject;
import com.soubao.common.constant.ShopConstant;
import com.soubao.service.UploadFileService;
import com.soubao.service.WxUserService;
import com.soubao.utils.WeChatUtil;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/upload")
@Api(value = "上传控制器", tags = {"上传相关控制器接口"})
public class UploadController {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private ShopConstant shopConstant;
    @Value("${spring.resources.static-locations}")
    private String staticLocation;

    @ApiOperation("上传图片")
    @PostMapping
    public SBApi singleFileUpload(@ApiParam("文件") @RequestParam("file") MultipartFile file,
                                  @ApiParam("图片类型") @RequestParam("type") String type, //上传图片类型/保存的相对路径
                                  SBApi sbApi) {
        sbApi.setResult(uploadFileService.uploadImage(file, type));
        return sbApi;
    }

    /**
     * 上传微信素材，并返回相应数据
     * @param multipartFile 媒体文件
     * @param type 文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @return 返回media_id等相关数据
     */
    @ApiOperation("上传微信素材")
    @RequestMapping("/we_chat/media")
    public JSONObject uploadImageToWechat(@RequestParam(value = "file") MultipartFile multipartFile,
                                     @RequestParam(value = "type") String type) {
        //  ../public/upload
        String uploadPath = staticLocation.substring(staticLocation.indexOf(":") + 1) + shopConstant.getImageUpload();
        String path = uploadPath+ "/wechat/cover/" + System.currentTimeMillis() + multipartFile.getOriginalFilename();
        File file = new File(path);
        String result = null;
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            result = WeChatUtil.addMaterialEverInter(wxUserService.getAccessToken(), file, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject resultJson = wxUserService.resultHandle(result);
        resultJson.put("result", path.substring(path.indexOf(uploadPath.substring(uploadPath.indexOf("/")))));
        return resultJson;
    }

    /**
     * 上传微信图文消息内的图片素材，微信图文素材ue编辑器用到
     * 本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。
     * @param file jpg/png格式，大小必须在1MB以下
     * @return
     */
    @ApiOperation("上传微信图文消息内的图片素材，微信图文素材ue编辑器用到")
    @PostMapping("/we_chat/image")
    public SBApi fileUploadWxImage(@ApiParam("文件") @RequestParam("file") MultipartFile file) {
        String imageUrl = uploadFileService.uploadImageWx(file);
        String wxImgUrl = WeChatUtil.getUpWxImgUrl(wxUserService.getAccessToken(), new File(imageUrl));
        return SBApi.builder().status(1).result(wxImgUrl).build();
    }

}




