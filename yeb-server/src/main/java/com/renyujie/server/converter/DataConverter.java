package com.renyujie.server.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author renyujie518
 * @version 1.0.0
 * @ClassName DataConverter.java
 * @Description 日期转换
 * @createTime 2021年12月28日 20:47:00
 */
@Component
public class DataConverter implements Converter<String, LocalDate> {


    @Override
    public LocalDate convert(String s) {
        try {
            return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
