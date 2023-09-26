package server.repositories;

import commons.entities.Tag;
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
import server.database.TagRepository;

public class TestTagRepository implements TagRepository {

    public final List<Tag> tags = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }


    @Override
    public Optional<Tag> findTagByBoard_IdAndId(long boardId, long id) {
        calledMethods.add("findTagByBoard_IdAndId");
        return tags.stream()
            .filter(cardlist -> cardlist.getBoard() != null)
            .filter(cardList -> cardList.getBoard().getId() == boardId)
            .filter(cardList -> id == cardList.getId())
            .findFirst();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {
        calledMethods.add("deleteAllInBatch");
        for(Tag tag : entities)
            tags.remove(tag);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    @Override
    public Tag getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    private Optional<Tag> find(Long id) {
        return tags.stream().filter(q -> q.getId() == id).findFirst();
    }

    @Override
    public Tag getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public List<Tag> findAll() {
        calledMethods.add("findAll");
        return tags;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Tag, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Tag> S save(S entity) {
        call("save");
        if(entity.getId() == 0) {
            entity.setId(tags.size() + 1);
            tags.add(entity);
        } else {
            tags.set(tags.indexOf(entity), entity);
        }

        return entity;
    }

    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        calledMethods.add("saveAll");
        entities.forEach(this::save);
        return null;
    }

    @Override
    public Optional<Tag> findById(Long aLong) {
        return tags.stream()
            .filter(cardList -> cardList.getId() == aLong)
            .findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        return find(aLong).isPresent();
    }

    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return tags.size();
    }

    @Override
    public void deleteById(Long aLong) {
        tags.removeIf(cl -> cl.getId() == aLong);
        // TODO Auto-generated method stub
        calledMethods.add("deleteById");
    }

    @Override
    public void delete(Tag entity) {
        calledMethods.add("delete");
        tags.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Tag> findAll(Sort sort) {
        calledMethods.add("findAll");
        return tags;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public int unlinkTag(@Param("tagId") long tagId){
        return 1;
    }
}

