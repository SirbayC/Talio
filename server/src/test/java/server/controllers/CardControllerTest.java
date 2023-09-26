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

import commons.dto.CardDto;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.mappers.CardMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repositories.TestBoardRepository;
import server.repositories.TestCardListRepository;
import server.repositories.TestCardRepository;
import server.services.CardService;

public class CardControllerTest {

    private TestBoardRepository boardRepository;
    private TestCardRepository cardRepository;
    private TestCardListRepository cardListRepository;
    private CardController sut;

    @BeforeEach
    public void setup() {
        boardRepository = new TestBoardRepository();
        cardRepository = new TestCardRepository();
        cardListRepository = new TestCardListRepository();
        CardService service = new CardService(cardListRepository, cardRepository, CardMapper.INSTANCE);
        sut = new CardController(service);
    }

    @Test
    public void failedUpdateNoBoard() {
        Assertions.assertEquals(0, boardRepository.count());
        Assertions.assertNull(
            sut.sendUpdate(0, new CardDto().setId(0).setCardListId(1L).setTitle("New Title"))
        );
    }

    @Test
    public void failedUpdateNoCard() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        //Verify board exists and no card
        Assertions.assertEquals(1, boardRepository.count());
        Assertions.assertEquals(0, cardRepository.count());
        Assertions.assertNull(
            sut.sendUpdate(board.getId(), new CardDto().setId(0).setCardListId(1L).setTitle("New Title"))
        );
    }

    @Test
    public void successfulUpdate() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        CardList cardList = new CardList().setBoard(board);
        cardListRepository.save(cardList);

        Card card = new Card().setBoard(board);
        cardRepository.save(card);

        //Verify board exists and no card
        Assertions.assertEquals(1, boardRepository.count());
        Assertions.assertEquals(1, cardRepository.count());
        Assertions.assertNotNull(
            sut.sendUpdate(board.getId(), new CardDto()
                .setId(card.getId())
                .setTitle("New Title")
                .setCardListId(cardList.getId()))
        );
    }

    @Test
    public void succesfulCardCreate() {
        Board board = new Board();
        boardRepository.save(board);
        CardList cardList = new CardList().setBoard(board);
        board.getCardLists().add(cardList);// Not taken care of automatically, because of test
        cardListRepository.save(cardList);

        Assertions.assertNotNull(sut.sendUpdate(board.getId(), new CardDto().setTitle("Card12").setCardListId(cardList.getId())));
        board.getCards().add(cardRepository.getById(1L));// Not taken care of automatically, because of test

        Assertions.assertEquals(1, cardRepository.count());
        Assertions.assertEquals("Card12", boardRepository.getById(1L).getCards().get(0).getTitle());
    }

    @Test
    public void failedCardCreate() {
        Board board = new Board();
        boardRepository.save(board);
        CardList cardList = new CardList().setBoard(board);
        cardListRepository.save(cardList);

        Assertions.assertNull(
            sut.sendUpdate(board.getId() + 1, new CardDto().setTitle("Card12").setCardListId(cardList.getId()))
        );
    }

    @Test
    public void succesfulCardDelete() {
        Board board = new Board();
        boardRepository.save(board);
        CardList cardList = new CardList().setBoard(board);
        cardListRepository.save(cardList);
        Card card = new Card().setCardList(cardList);
        cardRepository.save(card);

        Assertions.assertEquals(card.getId(), sut.sendUpdate(board.getId(), card.getId()));
        Assertions.assertEquals(0, cardRepository.count());
    }

    @Test
    public void failedCardDelete() {
        Board board = new Board();
        boardRepository.save(board);
        CardList cardList = new CardList().setBoard(board);
        cardListRepository.save(cardList);
        Card card = new Card().setCardList(cardList);
        cardRepository.save(card);
        Assertions.assertEquals(-1, sut.sendUpdate(card.getBoard().getId(), card.getId() + 1));
    }


    @Test
    public void databaseUse() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        CardList cardList = new CardList().setBoard(board);
        cardListRepository.save(cardList);

        Card card = new Card().setCardList(cardList);
        cardRepository.save(card);

        //Call method and verify database usage
        sut.sendUpdate(board.getId(), new CardDto().setId(card.getId()).setCardListId(cardList.getId()).setTitle("New Title"));
        Assertions.assertTrue(cardRepository.calledMethods.contains("findCardByBoard_IdAndId"));
    }

}