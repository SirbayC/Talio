package server.repositories;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import server.database.TaskRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTaskRepository implements TaskRepository{
    public final List<Task> tasks = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Task> findAll() {
        calledMethods.add("findAll");
        return tasks;
    }

    @Override
    public List<Task> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Task> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
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
    public <S extends Task> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {
        // TODO Auto-generated method stub

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
    public Task getOne(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Task getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public Task getReferenceById(Long aLong) {
        return null;
    }

    private Optional<Task> find(Long id) {
        return tasks.stream().filter(q -> q.getId() == id).findFirst();
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Task> S save(S entity) {
        call("save");
        if(entity.getId() == 0) {
            entity.setId(tasks.size() + 1);
            tasks.add(entity);
        } else {
            tasks.set(tasks.indexOf(entity), entity);
        }

        return entity;
    }

    @Override
    public Optional<Task> findById(Long id) {
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
        return tasks.size();
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Task entity) {
        if(tasks.contains(entity))
            tasks.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Task> long count(Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends Task> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends Task, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Task> findTaskByCardIdAndId(long cardId, Long id) {
        calledMethods.add("findTaskByCardIdAndId");
        return tasks.stream()
            .filter(c -> c.getCard().getId() == cardId)
            .filter(c -> id == c.getId())
            .findFirst();
    }

    @Override
    public Optional<Task> findTaskByBoardIdAndId(long boardId, Long id) {
        calledMethods.add("findTaskByBoardIdAndId");
        return tasks.stream()
            .filter(t -> t.getBoard().getId() == boardId)
            .filter(t -> id == t.getId())
            .findFirst();
    }

}
