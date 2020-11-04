package com.soubao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soubao.common.constant.WeChatContant;
import com.soubao.common.utils.HttpClientUtil;
//import com.soubao.constant.WeChatContant;
import com.soubao.dto.ArticleItem;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 微信工具类
 */
public class WeChatUtil {

    /**
     * 验证签名
     *
     * @param token     自己设置的token
     * @param signature 微信端发来的签名
     * @param timestamp 信端发来的时间戳
     * @param nonce     微信端发来的随机字符串
     * @return
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        String checkCode = StringUtils.join(arr);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");//进行SHA1加密
            md.update(checkCode.getBytes());
            // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
            return Hex.encodeHexString(md.digest()).equals(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

//        /**
//         * 将字节数组转换为十六进制字符串
//         *
//         * @param byteArray
//         * @return
//         */
//        private static String byteToStr(byte[] byteArray) {
//            String strDigest = "";
//            for (int i = 0; i < byteArray.length; i++) {
//                strDigest += byteToHexStr(byteArray[i]);
//            }
//            return strDigest;
//        }
//
//        /**
//         * 将字节转换为十六进制字符串
//         *
//         * @param mByte
//         * @return
//         */
//        private static String byteToHexStr(byte mByte) {
//            char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
//            char[] tempArr = new char[2];
//            tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
//            tempArr[1] = Digit[mByte & 0X0F];
//
//            String s = new String(tempArr);
//            return s;
//        }
//
//        private static void sort(String a[]) {
//            for (int i = 0; i < a.length - 1; i++) {
//                for (int j = i + 1; j < a.length; j++) {
//                    if (a[j].compareTo(a[i]) < 0) {
//                        String temp = a[i];
//                        a[i] = a[j];
//                        a[j] = temp;
//                    }
//                }
//            }
//        }

    /**
     * 解析微信发来的请求(xml)
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Element e : elements) {
            map.put(e.getName(), e.getText());
        }
        inputStream.close();
        return map;
    }

    public static String mapToXML(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        mapToXML2(map, sb);
        sb.append("</xml>");
        return sb.toString();
    }

    private static void mapToXML2(Map<String, Object> map, StringBuffer sb) {
        Set<String> set = map.keySet();
        for (String key : set) {
            Object value = map.get(key);
            if (value == null) {
                value = "";
            }
//                if (value.getClass().getName().equals("java.util.ArrayList")) {
            if (value instanceof ArrayList) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<").append(key).append(">");
                for (Object o : list) {
                    HashMap hm = (HashMap) o;
                    mapToXML2(hm, sb);
                }
                sb.append("</" + key + ">");
            } else {
                if (value instanceof HashMap) {
                    sb.append("<").append(key).append(">");
                    mapToXML2((HashMap) value, sb);
                    sb.append("</").append(key).append(">");
                } else {
                    sb.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
                }
            }
        }
//            Set set = map.keySet();
//            for (Iterator it = set.iterator(); it.hasNext();) {
//                String key = (String) it.next();
//                Object value = map.get(key);
//                if (null == value)
//                    value = "";
//                if (value.getClass().getName().equals("java.util.ArrayList")) {
//                    ArrayList list = (ArrayList) map.get(key);
//                    sb.append("<" + key + ">");
//                    for (int i = 0; i < list.size(); i++) {
//                        HashMap hm = (HashMap) list.get(i);
//                        mapToXML2(hm, sb);
//                    }
//                    sb.append("</" + key + ">");
//
//                } else {
//                    if (value instanceof HashMap) {
//                        sb.append("<" + key + ">");
//                        mapToXML2((HashMap) value, sb);
//                        sb.append("</" + key + ">");
//                    } else {
//                        sb.append("<" + key + "><![CDATA[" + value + "]]></" + key + ">");
//                    }
//
//                }
//            }
    }

    /**
     * 回复文本消息
     *
     * @param requestMap
     * @param content
     * @return
     */
    public static String sendTextMsg(Map<String, String> requestMap, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("ToUserName", requestMap.get(WeChatContant.FromUserName));
        map.put("FromUserName", requestMap.get(WeChatContant.ToUserName));
        map.put("MsgType", WeChatContant.RESP_MESSAGE_TYPE_TEXT);
        map.put("CreateTime", new Date().getTime());
        map.put("Content", content);
        return mapToXML(map);
    }

    /**
     * 回复图文消息
     *
     * @param requestMap
     * @param articleItemList
     * @return
     */
    public static String sendArticleMsg(Map<String, String> requestMap, List<ArticleItem> articleItemList) {
        if (articleItemList == null || articleItemList.isEmpty()) {
            return "";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ToUserName", requestMap.get(WeChatContant.FromUserName));
        map.put("FromUserName", requestMap.get(WeChatContant.ToUserName));
        map.put("MsgType", "news");
        map.put("CreateTime", new Date().getTime());
        List<Map<String, Object>> Articles = new ArrayList<>();
        for (ArticleItem articleItem : articleItemList) {
            Map<String, Object> item = new HashMap<>();
            Map<String, Object> itemContent = new HashMap<>();
            itemContent.put("Title", articleItem.getTitle());
            itemContent.put("Description", articleItem.getDescription());
            itemContent.put("PicUrl", articleItem.getPicUrl());
            itemContent.put("Url", articleItem.getUrl());
            item.put("item", itemContent);
            Articles.add(item);
        }
        map.put("Articles", Articles);
        map.put("ArticleCount", Articles.size());
        return mapToXML(map);
    }

    /**
     * 创建公众号自定义菜单
     *
     * @param accessToken
     * @param requestJSON
     * @return
     */
    public static String createMenu(String accessToken, String requestJSON) {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
        return HttpClientUtil.doPostJson(url, requestJSON);
    }

    /**
     * 上传图文消息内的图片获取URL
     *
     * @param file
     * @param accessToken
     * @return
     */
    public static String getUpWxImgUrl(String accessToken, File file) {
        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + accessToken;
        String jsonStr = formUpload(url, file);
        String returnUrl = "";
        if (!jsonStr.contains("errcode")) {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            returnUrl = jsonObject.get("url").toString();
        }
        return returnUrl;
    }

    /**
     * 添加微信永久素材
     *
     * @param accessToken
     * @param file        素材文件
     * @param type        素材类型
     * @return
     * @throws IOException
     */
    public static String addMaterialEverInter(String accessToken, File file, String type) throws IOException {
        //上传素材
        String path = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=" + type;
        URL realUrl = new URL(path);

        URLConnection con = realUrl.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        String sb = "--" + // 必须多两道线
                BOUNDARY +
                "\r\n" +
                "Content-Disposition: form-data;name=\"media\";filelength=\"" +
                file.length() + "\";filename=\"" + file.getName() + "\"\r\n" +
                "Content-Type:application/octet-stream\r\n\r\n";
        byte[] head = sb.getBytes(StandardCharsets.UTF_8);
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件以流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(StandardCharsets.UTF_8);// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuilder buffer = new StringBuilder();
        String result;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            // 定义BufferedReader输入流来读取URL的响应
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        }
        return result;
    }

    /**
     * 上传图片素材
     *
     * @param urlStr
     * @param file
     * @return
     */
    private static String formUpload(String urlStr, File file) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "----------" + System.currentTimeMillis(); // boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            String inputName = "";
            String filename = file.getName();
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            if (filename.endsWith(".png")) {
                contentType = "image/png";
            }
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }

            String str = "\r\n" + "--" + BOUNDARY + "\r\n" +
                    "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n" +
                    "Content-Type:" + contentType + "\r\n\r\n";
            out.write(str.getBytes());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuilder strBuf = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    /**
     * 获取公众号粉丝openids
     *
     * @param accessToken
     * @param nextOpenid
     * @return
     */
    public static String getUserOpenIds(String accessToken, String nextOpenid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=" + nextOpenid;
        return HttpClientUtil.doGet(url);
    }

    /**
     * 根据openID获取用户信息
     * @param accessToken
     * @param openid
     * @return
     */
    public static String getUserInfo(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
        return HttpClientUtil.doGet(url);
    }

    public static String getUserInfoBatch(String accessToken, String requestJSON){
        String url = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" + accessToken;
        return HttpClientUtil.doPostJson(url, requestJSON);
    }


}
