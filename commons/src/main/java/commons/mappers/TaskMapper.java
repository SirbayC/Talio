package commons.mappers;

import commons.dto.TaskDto;
import commons.entities.Task;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {

    // Global instance
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    /**
     * Map DTO to Entity
     *
     * @param taskDto DTO
     * @return Entity
     */
    Task toEntity(TaskDto taskDto);

    /**
     * Map Entity to DTO
     *
     * @param task Entity
     * @return DTO
     */
    TaskDto toDto(Task task);

    /**
     * Partially update Entity with DTO
     *
     * @param taskDto DTO
     * @param task Entity
     * @return Updated Entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);

}

