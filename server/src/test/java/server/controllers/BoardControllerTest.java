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

import commons.dto.BoardDto;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.mappers.BoardMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.async.DeferredResult;
import server.repositories.TestBoardRepository;
import server.repositories.TestCardListRepository;
import server.repositories.TestCardRepository;
import server.services.BoardService;

public class BoardControllerTest {

    private TestBoardRepository boardRepository;
    private TestCardListRepository cardListRepository;
    private TestCardRepository cardRepository;
    private BoardService service;

    private BoardController sut;

    @BeforeEach
    public void setup() {
        boardRepository = new TestBoardRepository();
        cardListRepository = new TestCardListRepository();
        cardRepository = new TestCardRepository();
        service = new BoardService(boardRepository, BoardMapper.INSTANCE);
        sut = new BoardController(boardRepository, BoardMapper.INSTANCE, service);
    }

    @Test
    public void poll() {
        DeferredResult<List<BoardDto>> result = sut.poll();

        Assertions.assertNotNull(service.getOrCreate(new BoardDto(0, "name", new ArrayList<>(),
            "black", "red", "black", "white", "Arial", new ArrayList<>())));
        Assertions.assertTrue(result.hasResult());
    }

    @Test
    public void get() {
        Board board = new Board();
        board.setName("M1");
        boardRepository.save(board);
        Assertions.assertTrue(sut.get(1).getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("M1", Objects.requireNonNull(sut.get(1).getBody()).getName());
    }

    @Test
    public void update() {
        Board board = new Board();
        board.setName("M1");
        boardRepository.save(board);
        Assertions.assertNotNull(sut.sendUpdate(1, new BoardDto().setName("M2").setId(1)));
        Assertions.assertEquals("M2", Objects.requireNonNull(sut.get(1).getBody()).getName());
    }

    @Test
    public void create() {
        Assertions.assertEquals("M2", sut.createBoard(new BoardDto().setName("M2")).getName());
        Assertions.assertEquals(1, sut.all().size());
    }


    @Test
    public void databaseUse() {
        sut.createBoard(new BoardDto().setName("Test"));
        Assertions.assertTrue(boardRepository.calledMethods.contains("save"));
    }

    @Test
    public void deleteTest() {
        Board board = new Board();
        boardRepository.save(board);

        CardList cl1 = new CardList().setBoard(board);
        CardList cl2 = new CardList().setBoard(board);
        board.getCardLists().addAll(List.of(cl1, cl2));// Not taken care of automatically, because of test
        cardListRepository.saveAll(List.of(cl1, cl2));

        Card card = new Card().setCardList(cl1);
        cl1.getCards().add(card);// Not taken care of automatically, because of test
        cardRepository.save(card);

        Assertions.assertTrue(sut.sendDeleteUpdate(board.getId()) != -1);
        Assertions.assertEquals(0, boardRepository.count());

        // Cascade is not up to the repository, but the database, so we cannot test this part
        // Assertions.assertEquals(0, cardListRepository.count());
        // Assertions.assertEquals(0, cardRepository.count());
    }
}