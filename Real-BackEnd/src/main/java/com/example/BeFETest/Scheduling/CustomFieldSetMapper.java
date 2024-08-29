package com.example.BeFETest.Scheduling;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomFieldSetMapper<T> implements FieldSetMapper<T> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Class<T> targetType;

    public CustomFieldSetMapper(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public T mapFieldSet(FieldSet fieldSet) throws BindException {
        T dto = null;
        try {
            // DTO를 기본 생성자로 생성
            dto = targetType.getDeclaredConstructor().newInstance();

            // LocalDate로 변환하여 설정
            String dateStr = fieldSet.readString("date").trim();
            LocalDate localDate = LocalDate.parse(dateStr.replace(" ", ""), dateFormatter);
            Field dateField = targetType.getDeclaredField("date");
            dateField.setAccessible(true);
            dateField.set(dto, localDate);

            for (String fieldName : new String[]{"closingPrice", "openingPrice", "highPrice", "lowPrice"}) {
                Field field = targetType.getDeclaredField(fieldName);
                field.setAccessible(true);
                String value = fieldSet.readString(fieldName);
                Double doubleValue = value != null && !value.isEmpty() ? Double.parseDouble(value.replace(",", "")) : null;
                field.set(dto, doubleValue);
            }

            // 나머지 필드 자동 설정
            for (String fieldName : new String[]{"tradingVolume", "fluctuatingRate"}) {
                Field field = targetType.getDeclaredField(fieldName);
                field.setAccessible(true);
                String value = fieldSet.readString(fieldName);
                field.set(dto, value);
            }

        } catch (Exception e) {
            throw new BindException(dto, "Failed to bind fields: " + e.getMessage());
        }

        return dto;
    }
}


// LocalDate로 변환
//        String dateStr = fieldSet.readString("date").trim();
//        dto.setDate(LocalDate.parse(dateStr, formatter));
//
//        // 나머지 필드 설정
//        dto.setClosingPrice(fieldSet.readString("closingPrice"));
//        dto.setOpeningPrice(fieldSet.readString("openingPrice"));
//        dto.setHighPrice(fieldSet.readString("highPrice"));
//        dto.setLowPrice(fieldSet.readString("lowPrice"));
//        dto.setTradingVolume(fieldSet.readString("tradingVolume"));
//        dto.setFluctuatingRate(fieldSet.readString("fluctuatingRate"));