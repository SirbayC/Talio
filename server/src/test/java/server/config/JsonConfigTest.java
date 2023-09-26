package server.config;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;

public class JsonConfigTest {

    @Test
    public void testJsonCustomizer() {
        JsonConfig jsonConfig = new JsonConfig();
        Jackson2ObjectMapperBuilderCustomizer customizer = jsonConfig.jsonCustomizer();
        assertNotNull(customizer);
    }
}
