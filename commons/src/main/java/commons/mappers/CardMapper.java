package commons.mappers;

import commons.dto.CardDto;
import commons.entities.Card;
import java.util.ArrayList;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {TagMapper.class}
)
public interface CardMapper {

    // Global Instance
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    /**
     * Map DTO to Entity
     *
     * @param cardDto DTO
     * @return Entity
     */
    Card toEntity(CardDto cardDto);

    /**
     * Map Entity to DTO
     *
     * @param card Entity
     * @return DTO
     */
    CardDto toDto(Card card);

    /**
     * Partially update Entity with DTO
     *
     * @param cardDto DTO
     * @param card    Entity
     * @return Updated Entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Card partialUpdate(CardDto cardDto, @MappingTarget Card card);

    /**
     * Link children after mapping from DTO to Entity
     *
     * @param card Entity
     */
    @AfterMapping
    default void linkTasks(@MappingTarget Card card) {
        if(card.getTaskList() == null)
            card.setTaskList(new ArrayList<>());

        card.getTaskList().forEach(task -> task.setCard(card));
    }

}