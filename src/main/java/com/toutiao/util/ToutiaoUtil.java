package com.toutiao.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by jianf on 2017/7/6.
 */
public class ToutiaoUtil {

    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);
    public static String[] IMAGE_FILE_EXTD=new String[]{"png","jpg","jpeg","bmp"};
    public static String IMAGE_DIR="D:/牛客项目课/image/";
    public static String TIOUTIAO_DOMAIN="http://172.23.18.239:8080/";

    //验证图片格式
    public  static boolean isFileAollowed(String fileName){
        for (String extd:IMAGE_FILE_EXTD){
            if (extd.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    //MD5加密
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    //JSONString
    public static String getJSONString(int code){
        JSONObject json =new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }

    public static String getJSONString(int code,String msg){
        JSONObject json =new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String,Object> map){
        JSONObject json =new JSONObject();
        json.put("code",code);
        for (Map.Entry<String,Object> entry:map.entrySet()){
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toJSONString();
    }

    //获取http请求的IP地址
    public static String getIpAddr(HttpServletRequest request){
        if (request == null)
            return null;
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip))
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            catch (UnknownHostException unknownhostexception) {
            }
        return ip;
    }



}
