package commons.mappers;

import commons.dto.TagDto;
import commons.entities.Tag;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    /**
     * Map DTO to Entity
     *
     * @param tagDto DTO
     * @return Entity
     */
    Tag toEntity(TagDto tagDto);

    /**
     * Map Entity to DTO
     *
     * @param tag Entity
     * @return DTO
     */
    TagDto toDto(Tag tag);


    /**
     * Partially update Entity with DTO
     *
     * @param tagDto DTO
     * @param tag   Entity
     * @return Updated Entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(TagDto tagDto, @MappingTarget Tag tag);
}
