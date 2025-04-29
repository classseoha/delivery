package com.example.delivery.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
매번 ObjectMapper를 새로 만들지 않고, 하나의 빈을 재사용 → 메모리 낭비 감소
설정(JavaTimeModule 등록 등)을 통일할 수 있음
향후 필요하면 공통 설정을 추가하기 편해짐 (ex. PropertyNamingStrategy, SerializationFeature 등)
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
