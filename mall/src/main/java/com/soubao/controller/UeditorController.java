package com.soubao.controller;

import com.alibaba.fastjson.JSONObject;
import com.soubao.baidu.ueditor.ActionEnter;
import com.soubao.common.constant.ShopConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/ueditor")
public class UeditorController {
    @Value("${spring.resources.static-locations}")
    private String imageUploadPath;
    @Autowired
    private ShopConstant shopConstant;

    @RequestMapping(value="/config")
    public void config(@RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String urlPath = imageUploadPath.substring(imageUploadPath.indexOf(":") + 1) + shopConstant.getImageUpload() + "/" + path;
        try {
            String exec = new ActionEnter(request, urlPath).exec();
            JSONObject stringToMap =  JSONObject.parseObject(exec);
            if(stringToMap.containsKey("url")){
                String imagePath = urlPath.replace("..","") + stringToMap.get("url");
                stringToMap.put("url", imagePath);
            }
            PrintWriter writer = response.getWriter();
            writer.write(stringToMap.toString());
            writer.flush();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/exec", method = RequestMethod.GET)
    public String exec(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getRealPath("/");
        return new ActionEnter( request, rootPath ).exec();
    }

    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    public Map<String, String> execPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
//        response.setHeader("Content-Type","test/html");
        response.setContentType("text/html;charset=utf-8");
        String rootPath = request.getRealPath("/");
        new ActionEnter( request, rootPath ).exec();
        String title = "ueditor-1.2";
        String group = "baidu";
        String url = "ueditor.jsp";
        Map<String,String> map1=new HashMap();
        map1.put("title",title);
        map1.put("group",group);
        map1.put("url",url);
//        map1.put("type",".jpg");
        map1.put("state","SUCCESS");
//        map1.put("size","0");
        map1.put("original",title);
        return map1;
    }

    @RequestMapping(value = "/exec", method = RequestMethod.PUT)
    public Map<String, String> execPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        String group = new ActionEnter( request, request.getRealPath("/")).revoke();
        String uploadscrawl = "uploadscrawl";
        String url = "Uploader.jsp";
        Map<String,String> map1=new HashMap();
        map1.put("uploadimage", uploadscrawl);
        map1.put("group",group);
        map1.put("url",url);
//        map1.put("type",".jpg");
        map1.put("state","SUCCESS");
//        map1.put("size","0");
        map1.put("original",uploadscrawl);
        return map1;
    }

}
