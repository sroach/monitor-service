package gy.roach.monitor.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Configuration for Jackson ObjectMapper.
 * This ensures proper handling of Java 8 date/time types.
 */
@Configuration
class JacksonConfig {

    /**
     * Configures the ObjectMapper with the JavaTimeModule to handle
     * Java 8 date/time types like LocalDateTime.
     *
     * @return The configured ObjectMapper
     */
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        return objectMapper
    }
}