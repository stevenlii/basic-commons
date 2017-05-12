package com.paymoon.basic.commons.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * json 解析类，通用于全项目
 */
public class JsonUtil {
	private static Logger logger = LogManager.getLogger();

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(sdf);
    }

    /**
     * 把bean转换成list map 集合类型
     * @param o
     * @return
     */
    public static Object beanToJsonObject(Object o) {
        return readValue(writeValueAsString(o), Map.class);
    }

    public static String writeValueAsString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (IOException e) {
            logger.error("object can not objectTranslate to json", e);
        }
        return null;
    }

    private static <T> T jsonStrToObject(String json, Class<T> cls) {
        try {
            return objectMapper.readValue(json, cls);
        } catch (IOException e) {
            logger.error("json cant be objectTranslate to object", e);
            return null;
        }
    }

    public static <T> T jsonDataToObject(String jsonStr, Class<T> cls) {
        if (StringUtils.isNotBlank(jsonStr)) {
            T data = jsonStrToObject(jsonStr, cls);
            return data;
        } else {
            return null;
        }
    }

    public static <T> List<T> readValue(String jsonStr, Class<?> clazz) {
        List<T> list = Lists.newArrayList();
        try {
            // 指定容器结构和类型（这里是ArrayList和clazz）
            TypeFactory t = TypeFactory.defaultInstance();
            list = objectMapper.readValue(jsonStr,
                    t.constructCollectionType(ArrayList.class, clazz));
        } catch (IOException e) {
            logger.error("反序列化序列化attributes，从Json到List报错", e);
        }
        return list;
    }

    public static Map<String,?> readValue(String attributes) {
        try {
            return objectMapper.readValue(attributes, new TypeReference<HashMap<java.lang.String,?>>() {
			});
        } catch (IOException e) {
            logger.error("反序列化序列化attributes，从Json到HashMap报错", e);
        }
        return new HashMap<String, Object>();
    }
    public static void main(String[] args) {
    	String aa;
		final Map<String, String> hashmpa = new HashMap<>();
		hashmpa.put("aa", "bb");
		hashmpa.put("cc", "b2");
		hashmpa.put("dd", "b3");
		String hashmpaString = writeValueAsString(hashmpa);
		Map<String, String>  newMap = (Map<String, String>) readValue(hashmpaString);
		System.out.println(newMap);
	    
	}


}