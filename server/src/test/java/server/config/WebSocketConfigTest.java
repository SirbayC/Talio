package server.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WebSocketConfigTest {

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Test
    public void testRegisterStompEndpoints() {
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        webSocketConfig.registerStompEndpoints(registry);
        verify(registry).addEndpoint("/websocket");
    }

    @Test
    public void testConfigureMessageBroker() {
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
        webSocketConfig.configureMessageBroker(registry);
        verify(registry).enableSimpleBroker("/out");
        verify(registry).setApplicationDestinationPrefixes("/in");
    }
}
