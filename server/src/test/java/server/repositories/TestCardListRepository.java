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
package server.repositories;

import commons.entities.CardList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import server.database.CardListRepository;

public class TestCardListRepository implements CardListRepository {

    public final List<CardList> cardLists = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<CardList> findAll() {
        calledMethods.add("findAll");
        return cardLists;
    }

    @Override
    public List<CardList> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CardList> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends CardList> List<S> saveAll(Iterable<S> entities) {
        calledMethods.add("saveAll");
        entities.forEach(this::save);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends CardList> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends CardList> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<CardList> entities) {
        // TODO Auto-generated method stub
        calledMethods.add("deleteAllInBatch");
        for(CardList cardList : entities)
            cardLists.remove(cardList);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub

    }

    @Override
    public CardList getOne(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CardList getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public CardList getReferenceById(Long aLong) {
        return null;
    }

    private Optional<CardList> find(Long id) {
        return cardLists.stream().filter(q -> q.getId() == id).findFirst();
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<CardList> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends CardList> S save(S entity) {
        call("save");
        if(entity.getId() == 0) {
            entity.setId(cardLists.size() + 1);
            cardLists.add(entity);
        } else {
            cardLists.set(cardLists.indexOf(entity), entity);
        }

        return entity;
    }

    @Override
    public Optional<CardList> findById(Long id) {
        return cardLists.stream()
            .filter(cardList -> cardList.getId() == id)
            .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return find(id).isPresent();
    }

    @Override
    public long count() {
        return cardLists.size();
    }

    @Override
    public void deleteById(Long id) {
        cardLists.removeIf(cl -> cl.getId() == id);
        // TODO Auto-generated method stub
        calledMethods.add("deleteById");
    }

    @Override
    public void delete(CardList entity) {
        calledMethods.add("delete");
        cardLists.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(Iterable<? extends CardList> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends CardList> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends CardList> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends CardList> long count(Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends CardList> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends CardList, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CardList> findCardListByBoard_IdAndId(long boardId, long id) {
        calledMethods.add("findCardListByBoard_IdAndId");
        return cardLists.stream()
            .filter(cardList -> cardList.getBoard().getId() == boardId)
            .filter(cardList -> id == cardList.getId())
            .findFirst();
    }

}