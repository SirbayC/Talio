package commons.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import commons.dto.TagDto;
import commons.entities.Tag;
import org.junit.jupiter.api.Test;

class TagMapperTest {

    @Test
    void toEntity() {
        TagDto tagDto = new TagDto().setName("q").setColour("w").setId(1);
        Tag tag = TagMapper.INSTANCE.toEntity(tagDto);
        assertEquals(tagDto.getName(), tag.getName());
        assertEquals(tagDto.getId(), tag.getId());
        assertEquals(tagDto.getColour(), tag.getColour());
    }

    @Test
    void toDto() {
        Tag tag = new Tag().setName("q").setColour("w").setId(1);
        TagDto tagDto = TagMapper.INSTANCE.toDto(tag);
        assertEquals(tag.getName(), tagDto.getName());
        assertEquals(tag.getId(), tagDto.getId());
        assertEquals(tag.getColour(), tagDto.getColour());
    }

    @Test
    void nullValues() {
        assertNull(TagMapper.INSTANCE.toEntity(null));
        assertNull(TagMapper.INSTANCE.toDto(null));
        assertNull(TagMapper.INSTANCE.partialUpdate(null, null));
    }

    @Test
    void partialUpdate() {
        Tag tag = new Tag();
        TagDto tagDto = new TagDto().setName("q").setColour("w").setId(1);
        TagMapper.INSTANCE.partialUpdate(tagDto, tag);
        assertEquals(tag.getName(), "q");
        assertEquals(tag.getColour(), "w");
    }
}