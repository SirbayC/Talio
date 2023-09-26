package server.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

    /**
     * Customize mapper by including LocalDate serializer/deserializer
     *
     * @return Jackson builder customizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializers(
            new LocalDateSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ).deserializers(
            new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        ).build();
    }

}
