package com.Batman.configuration;

import com.Batman.entity.Game;
import com.batman.elastic.BaseEsDeserializer;
import com.batman.elastic.BaseEsSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SimpleModule simpleGameSerializerModule = new SimpleModule();
        simpleGameSerializerModule.addSerializer(new BaseEsSerializer<>(Game.class));
        simpleGameSerializerModule.addDeserializer(Game.class, new BaseEsDeserializer<>(Game.class));
        objectMapper.registerModule(simpleGameSerializerModule);
        return objectMapper;
    }


}
