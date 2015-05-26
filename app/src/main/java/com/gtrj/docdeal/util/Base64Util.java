package com.gtrj.docdeal.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

/**
 * Created by zhang77555 on 2015/3/3.
 */
public class Base64Util {

    public static String decoderBase64FileWithFileName(String base64Code,
                                                       String fileName) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            return "unavailable";
        }
        File file = new File(ContextString.FilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = ContextString.FilePath +File.separator + fileName;
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

    public static String decoderBase64FileWithFileName(String base64Code,
                                                       String fileName,Context context) {
        String filePath = context.getFilesDir() +File.separator + fileName;
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

