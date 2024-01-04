package ua.thecompany.eloguru.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocActuator {
    @Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder().group("Actuator")
                .pathsToMatch("/actuator/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder().group("Public")
                .pathsToMatch("/**")
                .build();
    }
}
