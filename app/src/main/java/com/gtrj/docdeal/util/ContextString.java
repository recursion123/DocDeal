package com.gtrj.docdeal.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhang77555 on 2014/12/24.
 */
public class ContextString {

    public static final String FilePath= Environment.getExternalStorageDirectory().getPath()+ File.separator+ "DOCDEAL";

    public static final String WebServiceURL = "http://61.183.239.158:8881/services/NodeGWWebService?wsdl";
    public static final String NameSpace = "http://highrun.com";
    public static final String Login = "getUserExist";
    public static final String DocMainList = "getWaitTodo";
    public static final String SearchCondition ="getSearchCondition";
    public static final String DocMainDetail = "getMobileGWInfo";
    public static final String DocMainDetailAccessory = "getFJFileBase64Content";
    public static final String DocContacts = "getUserByOrgidAndBooktype";
    public static final String DocContactsDetail = "getInfoByUserId";
}
