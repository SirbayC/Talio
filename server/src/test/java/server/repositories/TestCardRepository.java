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

import commons.entities.Card;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.data.repository.query.Param;
import server.database.CardRepository;

public class TestCardRepository implements CardRepository {

    public final List<Card> cards = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Card> findAll() {
        calledMethods.add("findAll");
        return cards;
    }

    @Override
    public List<Card> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Card> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
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
    public <S extends Card> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {
        // TODO Auto-generated method stub
        calledMethods.add("deleteAllInBatch");
        for(Card card : entities)
            cards.remove(card);
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
    public Card getOne(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Card getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public Card getReferenceById(Long aLong) {
        return null;
    }

    private Optional<Card> find(Long id) {
        return cards.stream().filter(q -> q.getId() == id).findFirst();
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Card> S save(S entity) {
        call("save");
        if(entity.getId() == 0) {
            entity.setId(cards.size() + 1);
            cards.add(entity);
        } else {
            cards.set(cards.indexOf(entity), entity);
        }

        return entity;
    }

    @Override
    public Optional<Card> findById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return find(id).isPresent();
    }

    @Override
    public long count() {
        return cards.size();
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete(Card entity) {
        calledMethods.add("delete");
        cards.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(Iterable<? extends Card> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Card> long count(Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends Card, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Card> findCardByBoard_IdAndId(long boardId, long id) {
        calledMethods.add("findCardByBoard_IdAndId");
        return cards.stream()
            .filter(c -> c.getBoard().getId() == boardId)
            .filter(c -> id == c.getId())
            .findFirst();
    }

    @Override
    public int linkCardToTag(long cardId, long tagId){
        return 1;
    }

    @Override
    public int unlinkCardFromTag(long cardId, long tagId){
        return 1;
    }

    @Override
    public int unlinkCard(long cardId){
        return 1;
    }
}