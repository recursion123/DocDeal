package com.gtrj.docdeal.net;

import java.util.Map;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class WebService {

    private int _setHttpTimeOut = 10 * 10000;
    private Boolean _isdebug = false;
    private Boolean _iswritelog = true;

    /**
     * 获取WebService数据，返回SoapObject对象。
     *
     * @param Url          WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
     * @param NameSpace    WebService的服务的命名空间，可以WSDL数据中找到 (http://****.com.cn/)
     * @param MethodName   WebService的调用函数方法名称(getDataMethod)
     * @param RequestDatas 请求服务需要提交的数据集
     * @return 服务返回SoapObject对象
     */
    public SoapObject GetObject(String Url, String NameSpace,
                                String MethodName, Map<String, String> RequestDatas) {
        try {
            SoapObject soap = new SoapObject(NameSpace, MethodName);
            // 系统日志输出
            if (_iswritelog) {
                System.out.println("[URL] : " + Url);
                System.out.println("[NameSpace] : " + NameSpace);
                System.out.println("[MethodName] : " + MethodName);
                System.out.println("[SOAP Action] : " + NameSpace + MethodName);
            }
            // 设置WebService提交的数据集
            if (RequestDatas != null && !RequestDatas.isEmpty()) {
                for (Map.Entry<String, String> entry : RequestDatas.entrySet()) {
                    soap.addProperty(entry.getKey(), entry.getValue());
                }
            }
            // 初始化数据请求
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.bodyOut = soap;
            envelope.setOutputSoapObject(soap);
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = false;
            // 发起Web请求
            HttpTransportSE http = new HttpTransportSE(Url, _setHttpTimeOut);
            http.debug = _isdebug;
            http.call(NameSpace + MethodName, envelope);
            // 获取Web请求结果， 数据需要从 result.getProperty(0) 获取
            SoapObject result = (SoapObject) envelope.bodyIn;
            if (_iswritelog)
                System.out.println("[SOAP.getPropertyCount] : "
                        + result.getPropertyCount());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (_iswritelog)
                System.err.println("[Http Exception] : " + e.getMessage());
        }
        return null;
    }
}

