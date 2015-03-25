package com.gtrj.docdeal.util;

import java.io.Serializable;
import java.util.Map;

/**
 * 序列化map供Bundle传递map使用
 * Created by zhang77555 on 2015/3/20.
 */
public class SerializableMap implements Serializable {

    private Map<String, Map<String,String>> map;

    public Map<String, Map<String,String>> getMap() {
        return map;
    }

    public void setMap(Map<String, Map<String,String>> map) {
        this.map = map;
    }
}

