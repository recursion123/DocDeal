/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.Base64Util;
import com.gtrj.docdeal.util.ContextString;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gtrj.gallerylibrary.GalleryWidget.BasePagerAdapter;
import gtrj.gallerylibrary.GalleryWidget.FilePagerAdapter;
import gtrj.gallerylibrary.GalleryWidget.GalleryViewPager;


public class GalleryFileActivity extends Activity {
    private String gwSign;
    private String fileName;
    private String path;
    private int currentPage = 1;
    private int loadPage = 0;
    private int totalPage;
    private GalleryViewPager mViewPager;
    private FilePagerAdapter pagerAdapter;
    private List<String> items;
    private Context context;

    private Boolean isInit = true;
    private Boolean[] isDownload;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_picture);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        context = this;

        gwSign = getIntent().getStringExtra("docId");
        fileName = getIntent().getStringExtra("accessoryName");

        Message msg = msgHandler.obtainMessage();
        msg.arg1 = 1;
        msgHandler.sendMessage(msg);
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (isInit) {
                                path = parseXml(getData(currentPage));
                                isDownload = new Boolean[totalPage];
                                for (int i = 0; i < totalPage; i++) {
                                    if (i == 0) {
                                        isDownload[i] = true;
                                    } else {
                                        isDownload[i] = false;
                                    }
                                }
                                Message msg = msgHandler.obtainMessage();
                                msg.arg1 = 2;
                                msgHandler.sendMessage(msg);
                            } else {
                                if (loadPage + 1 < totalPage && !isDownload[loadPage + 1]) {
                                    loadPage += 1;
                                    path = parseXml(getData(loadPage + 1));
                                    isDownload[loadPage + 1] = true;
                                    Message msg = msgHandler.obtainMessage();
                                    msg.arg1 = 3;
                                    msgHandler.sendMessage(msg);
                                }
                            }
                        }
                    });
                    t.start();
                    break;
                case 2:
                    items = new ArrayList<>();
                    items.add(path);
                    pagerAdapter = new FilePagerAdapter(context, items);
                    pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
                        @Override
                        public void onItemChange(int currentPosition) {
                            if (currentPage < totalPage) {
                                currentPage = currentPosition + 1;
                                Log.e("currentPage", currentPage + "");
                                Message msg = msgHandler.obtainMessage();
                                msg.arg1 = 1;
                                msgHandler.sendMessage(msg);
                            } else {
                                Toast.makeText(context, "到达最后一页", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
                    mViewPager.setOffscreenPageLimit(3);
                    mViewPager.setAdapter(pagerAdapter);
                    isInit = false;
                    break;
                case 3:
                    items.add(path);
                    pagerAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private String parseXml(String xml) {
        try {
            File file = new File(ContextString.FilePath + File.separator + "xml.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(xml.getBytes());
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Element totalPageElement = root.element("总页数");
            Element currentPageElement = root.element("当前页数");
            Element pictureElement = root.element("内容");
            totalPage = Integer.parseInt(totalPageElement.getText());
            String pageNum = currentPageElement.getText();
            String path = Base64Util.decoderBase64FileWithFileName(pictureElement.getText(), pageNum + ".jpg", this);
            return path;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getData(int pageNum) {
        Map<String, String> requestDatas = new HashMap<>();
        requestDatas.put("gwSign", gwSign);
        requestDatas.put("fileName", fileName);
        requestDatas.put("pageNum", String.valueOf(pageNum));
        SoapObject obj = new WebService().GetObject(
                ContextString.WebServiceURL,
                ContextString.NameSpace,
                ContextString.DocMainDetailAccessory,
                requestDatas);
        return obj.getProperty(ContextString.DocMainDetailAccessory + "Return").toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}