package com.qiao.androidlab.lightreader.RequestUtil;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CRL;
import java.util.Random;

/**
 * UploadUtil
 *
 * @author: 乔云瑞
 * @time: 2016/3/19 17:18
 * <p/>
 * 上传图片的工具类
 */
public class UploadUtil {

    private static final String END = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "******";

    /**
     * 上传文件
     *
     * @param url
     * @param file
     */
    public static String upload(URL url, File file) {
        String result = "empty";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //设置每次传输的流的大小，可以有效的防止手机因内存不足崩溃
            //此方法用于在预先不知道内容长度时启用没有进行内部还从的HTTP请求正文的流
            httpURLConnection.setChunkedStreamingMode(128 * 1024);  //128K
            //允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            //使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + END);
            dos.writeBytes("Content-Disposition: form-data; " +
                    "name=\"uploadedfile\"; filename=\"" + file.getName() + "\"" + END);
            dos.writeBytes(END);

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[8192]; //8K
            int count = 0;
            //读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(END);
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + END);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
            dos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
