package com.qiao.androidlab.lightreader.RequestUtil;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HttpUtil
 *
 * @author: 乔云瑞
 * @time: 2016/3/13 20:16
 */
public class HttpUtil {

    /**
     * 发送POST请求
     *
     * @param path
     * @param params
     * @return
     */
    public static String sendPostRequest(String path, String params) {
        String result = "";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setUseCaches(false);
            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                result = new String(message.toByteArray());
            }
        } catch (MalformedURLException e) {
            Log.i("RegisterFragment", "ERROR1");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("RegisterFragment", "ERROR2");
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 截取json格式的字符串
     *
     * @return
     */
    public static String getJsonString(String str) {
        /*String result = "";
        int i;
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '<') {
                break;
            }
        }
        result = str.substring(0, i);*/
        String[] result;
        result = str.split("<script");
        return result[0];
    }

}
