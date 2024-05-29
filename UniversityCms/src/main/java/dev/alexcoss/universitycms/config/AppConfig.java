package dev.alexcoss.universitycms.config;

import dev.alexcoss.universitycms.service.client.DataRestClientImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    DataRestClientImpl dataRestClient(@Value("${university-cms.service.data-generator.url:http://localhost:8085}") String serviceUrl) {
        return new DataRestClientImpl(RestClient.builder()
            .baseUrl(serviceUrl)
            .build());
    }
}
