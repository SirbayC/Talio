package commons.mappers;

import commons.dto.CardListDto;
import commons.entities.CardList;
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
    uses = {CardMapper.class}
)
public interface CardListMapper {

    // Global Instance
    CardListMapper INSTANCE = Mappers.getMapper(CardListMapper.class);

    /**
     * Map DTO to Entity
     *
     * @param cardListDto DTO
     * @return Entity
     */
    CardList toEntity(CardListDto cardListDto);

    /**
     * Map Entity to DTO
     *
     * @param cardList Entity
     * @return DTO
     */
    CardListDto toDto(CardList cardList);

    /**
     * Partially update Entity with DTO
     *
     * @param cardListDto DTO
     * @param cardList    Entity
     * @return Updated Entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardList partialUpdate(CardListDto cardListDto, @MappingTarget CardList cardList);

    /**
     * Link children after mapping from DTO to Entity
     *
     * @param cardList Entity
     */
    @AfterMapping
    default void linkCards(@MappingTarget CardList cardList) {
        if(cardList.getCards() == null)
            cardList.setCards(new ArrayList<>());

        cardList.getCards().forEach(card -> card.setCardList(cardList));
    }

}