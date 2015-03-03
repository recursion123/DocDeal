package com.gtrj.docdeal.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

/**
 * Created by zhang77555 on 2015/3/3.
 */
public class Base64Util {
    /**
     * Base64字节流转化为指定tye类型文件 文件名时间随机函数
     *
     * @param base64Code
     * @param type
     * @return 返回文件路径
     */
    public static String decoderBase64File(String base64Code, String type) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            return "unavailable";
        }
        File file = new File("/sdcard/myDoc/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = "tempfile." + type;
        String filePath = "/sdcard/myDoc/" + name;
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(buffer);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * Base64字节流转化为指定类型文件
     *
     * @param base64Code
     * @param fileName
     * @return 返回文件路径
     */
    public static String decoderBase64FileWithFileName(String base64Code,
                                                       String fileName) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            return "unavailable";
        }
        File file = new File("/sdcard/myDoc/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = "/sdcard/myDoc/" + fileName;
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(buffer);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}

