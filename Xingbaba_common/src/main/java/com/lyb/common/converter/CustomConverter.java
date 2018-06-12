package com.lyb.common.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义converter转换器
 * 去掉前后空格
 * Converter<S,T>  S:页面上的数据类型    T:转换后的数据类型
 * Created by lin on 2018/4/18.
 */

public class CustomConverter implements Converter<String,String>{

    @Override
    public String convert(String source) { //也就是结果要么是有值要么是 null ,不可能是空串
        try {
            if(null != source){
                source.trim();
                if(!"".equals(source)){
                    return source;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
