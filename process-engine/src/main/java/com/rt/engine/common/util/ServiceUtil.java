package com.rt.engine.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.dto.ServiceField;

public class ServiceUtil {

    public static final String STRING = String.class.getSimpleName().toLowerCase(Locale.ROOT);
    public static final String OBJECT = Object.class.getSimpleName().toLowerCase(Locale.ROOT);
    public static final String INT = int.class.getSimpleName().toLowerCase(Locale.ROOT);
    public static final String BOOLEAN = Boolean.class.getSimpleName().toLowerCase(Locale.ROOT);
    public static final String ARRAY = Array.class.getSimpleName().toLowerCase(Locale.ROOT);
    public static final String DOUBLE = Double.class.getSimpleName().toLowerCase(Locale.ROOT);

    /**
     * 获取Json对象属性
     * 
     * @param jsonObject
     *            对象
     * @return 属性集合
     */
    public static List<ServiceField> convertServiceField(JSONObject jsonObject) {
        List<ServiceField> serviceFieldList = new ArrayList<>();
        if (jsonObject == null) {
            return serviceFieldList;
        }
        // for(String paramName :jsonObject.keySet()){
        // Object value = jsonObject.get(paramName);
        // if(value ==null)
        // if (value != null) {
        // ServiceField serviceField=new ServiceField();
        // serviceField.setParamName(paramName)
        // .ServiceFieldBuilder fieldBuilder = ServiceField.builder().name(name);
        // if (value instanceof String) {
        // fieldBuilder.type(STRING);
        // fieldBuilder.example(value);
        // } else if (value instanceof Integer) {
        // fieldBuilder.type(INT);
        // fieldBuilder.example(value);
        // } else if (value instanceof Boolean) {
        // fieldBuilder.type(BOOLEAN);
        // fieldBuilder.example(value);
        // } else if (value instanceof Double) {
        // fieldBuilder.type(DOUBLE);
        // fieldBuilder.example(value);
        // } else if (isObject(JSON.toJSONString(value))) {
        // fieldBuilder.type(OBJECT);
        // fieldBuilder.children(
        // ServiceUtil.convertServiceField(JSONObject.parseObject(JSON.toJSONString(value))));
        // } else if (isArray(value)) {
        // fieldBuilder.type(ARRAY);
        // JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(value));
        // if (jsonArray != null && jsonArray.size() > 0) {
        // fieldBuilder.children(ServiceUtil
        // .convertServiceField(JSONObject.parseObject(JSON.toJSONString(jsonArray.get(0)))));
        // }
        // }
        // serviceFieldList.add(fieldBuilder.build());
        // }
        // });
        return serviceFieldList;
    }

    /**
     * 判断对象
     * 
     * @param value
     *            json值
     * @return true/false
     */
    private static boolean isObject(Object value) {
        try {
            JSON.parseObject(JSON.toJSONString(value));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 判断集合
     *
     * @param value
     *            json值
     * @return true/false
     */
    private static boolean isArray(Object value) {
        try {
            JSON.parseArray(JSON.toJSONString(value));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
