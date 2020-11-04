package com.soubao.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.soubao.utils.RedisUtil;
import com.soubao.utils.ShopStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

/**
 * 前端控制器
 *
 * @author dyr
 * @since 2020-03-13
 */
@Slf4j
@RestController
public class AuthController {


    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private RedisUtil redisUtil;

    private static final String VERIFYKEY = "_verification";

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }

    /**
     * 验证码
     *
     * @throws IOException
     */
    @GetMapping("verification")
    public String getVerification(HttpServletRequest request) {
        ByteArrayOutputStream jpegOutputStream = null;
        try {
            jpegOutputStream = new ByteArrayOutputStream();
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            String ip = ShopStringUtil.getIpAddr(request);
            //log.info("ip：" + ip + "验证码：" + createText);
            redisUtil.set(ip + VERIFYKEY, createText);
            redisUtil.expire(ip + VERIFYKEY, 60);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
            String base64 = Base64.getEncoder().encodeToString(jpegOutputStream.toByteArray());
            return "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (jpegOutputStream != null) {
                    jpegOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping("verification")
    public boolean checkVerification(
            @RequestParam("verification") String verification, HttpServletRequest request) {
        String ip = ShopStringUtil.getIpAddr(request);
        if (!redisUtil.hasKey(ip + VERIFYKEY)) {
            //log.info("ip：" + ip + "验证码不存在：");
            return false;
        }
        if (!redisUtil.get(ip + VERIFYKEY).toString().equals(verification)) {
            //log.info("ip：" + ip + "验证码：" + redisUtil.get(ip + VERIFYKEY).toString());
            return false;
        }
        return true;
    }
}
