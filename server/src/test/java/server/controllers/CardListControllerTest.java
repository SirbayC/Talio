/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.controllers;

import commons.dto.CardListDto;
import commons.entities.Board;
import commons.entities.CardList;
import commons.mappers.CardListMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repositories.TestBoardRepository;
import server.repositories.TestCardListRepository;
import server.services.CardListService;

public class CardListControllerTest {

    private TestBoardRepository boardRepository;
    private TestCardListRepository cardListRepository;
    private CardListController sut;

    @BeforeEach
    public void setup() {
        boardRepository = new TestBoardRepository();
        cardListRepository = new TestCardListRepository();
        CardListService service = new CardListService(boardRepository, cardListRepository, CardListMapper.INSTANCE);
        sut = new CardListController(service);
    }

    @Test
    public void databaseUse() {
        Board board = new Board();
        boardRepository.save(board);
        sut.sendUpdate(board.getId(), new CardListDto().setTitle("Test List"));

        Assertions.assertTrue(cardListRepository.calledMethods.contains("save"));
    }

    @Test
    public void deleteCardListSuccess() {
        Board b = new Board();
        boardRepository.save(b);
        CardList l1 = new CardList().setBoard(b).setTitle("Todo");
        cardListRepository.save(l1);
        CardList l2 = new CardList().setBoard(b).setTitle("In Process");
        cardListRepository.save(l2);
        CardList l3 = new CardList().setBoard(b).setTitle("Done");
        cardListRepository.save(l3);

        sut.sendUpdate(
            boardRepository.findAll().get(0).getId(),
            cardListRepository.findAll().get(0).getId()
        );
        Assertions.assertTrue(cardListRepository.calledMethods.contains("delete"));
        Assertions.assertEquals(
            cardListRepository.findAll(),
            List.of(l2, l3)
        );
    }

    @Test
    public void deleteCardListNoCl() {
        Board b = new Board();
        boardRepository.save(b);
        sut.sendUpdate(
            boardRepository.findAll().get(0).getId(),
            0
        );

        Assertions.assertTrue(cardListRepository.calledMethods.contains("findCardListByBoard_IdAndId"));
        Assertions.assertFalse(cardListRepository.calledMethods.contains("delete"));
    }

    @Test
    public void updateCardListSuccess() {
        Board b = new Board();
        boardRepository.save(b);

        CardList l1 = new CardList().setBoard(b).setTitle("Todo");
        CardList l2 = new CardList().setBoard(b).setTitle("In Process");
        CardList l3 = new CardList().setBoard(b).setTitle("Done");
        cardListRepository.saveAll(List.of(l1, l2, l3));

        Assertions.assertNotNull(
            sut.sendUpdate(
                b.getId(),
                new CardListDto().setId(l1.getId()).setTitle("ToDo2")
            )
        );
        Assertions.assertEquals(
            cardListRepository.findById(l1.getId()).get().getTitle(),
            "ToDo2"
        );
    }

    @Test
    public void createCardList() {
        Board b = new Board();
        boardRepository.save(b);

        CardList l1 = new CardList().setBoard(b).setTitle("Todo");
        cardListRepository.save(l1);

        Assertions.assertNotNull(
            sut.sendUpdate(
                b.getId(),
                new CardListDto().setTitle("ToDo2")
            )
        );
        Assertions.assertEquals(2, cardListRepository.cardLists.size());
    }

    @Test
    public void updateCardListNoBoard() {

        Assertions.assertNull(
            sut.sendUpdate(
                0,
                new CardListDto().setId(1L).setTitle("ToDo2")
            )
        );
    }
}