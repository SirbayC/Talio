package server.config;

import commons.mappers.BoardMapper;
import commons.mappers.CardListMapper;
import commons.mappers.CardMapper;
import commons.mappers.TagMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MapperConfigTest {

    @Autowired
    private MapperConfig mapperConfig;

    @Test
    void boardMapper() {
        BoardMapper boardMapper = mapperConfig.boardMapper();
        assertTrue(boardMapper instanceof BoardMapper);
    }

    @Test
    void cardListMapper() {
        CardListMapper cardListMapper = mapperConfig.cardListMapper();
        assertTrue(cardListMapper instanceof CardListMapper);
    }

    @Test
    void cardMapper() {
        CardMapper cardMapper = mapperConfig.cardMapper();
        assertTrue(cardMapper instanceof CardMapper);
    }

    @Test
    void tagMapper() {
        TagMapper tagMapper = mapperConfig.tagMapper();
        assertTrue(tagMapper instanceof TagMapper);
    }
}