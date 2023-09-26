package client.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerExistsTest {

    @Test
    void testEnumValues() {
        assertEquals(7, ServerExists.values().length);
        assertEquals(ServerExists.EMPTY_ADDRESS, ServerExists.valueOf("EMPTY_ADDRESS"));
        assertEquals(ServerExists.INVALID_ADDRESS, ServerExists.valueOf("INVALID_ADDRESS"));
        assertEquals(ServerExists.REQUEST_EXCEPTION, ServerExists.valueOf("REQUEST_EXCEPTION"));
        assertEquals(ServerExists.INVALID_SERVER, ServerExists.valueOf("INVALID_SERVER"));
        assertEquals(ServerExists.VALID, ServerExists.valueOf("VALID"));
        assertEquals(ServerExists.ADMIN, ServerExists.valueOf("ADMIN"));
        assertEquals(ServerExists.INVALID_PASSWORD, ServerExists.valueOf("INVALID_PASSWORD"));
    }

}
