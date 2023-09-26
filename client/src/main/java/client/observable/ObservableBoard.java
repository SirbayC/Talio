package client.observable;

import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import client.utils.ServerUtils;
import commons.dto.BoardDto;
import commons.dto.CardDto;
import commons.dto.CardListDto;
import commons.dto.CardTagDto;
import commons.dto.TagDto;
import commons.dto.TaskDto;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import commons.entities.Task;
import commons.mappers.BoardMapper;
import commons.mappers.CardListMapper;
import commons.mappers.CardMapper;
import commons.mappers.TagMapper;
import commons.mappers.TaskMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
public class ObservableBoard {

    private ServerUtils server;
    private ObservableObject<Board> board;

    @Getter
    @Setter
    private ObservableValueMap<Long, CardList> cardLists;

    @Getter
    @Setter
    private ObservableValueMap<Long, Card> cards;

    @Getter
    @Setter
    private ObservableValueMap<Long, Tag> tags;

    @Setter
    private ObservableValueMap<Long, Task> tasks;

    /**
     * Change the board to observe
     *
     * @param server server
     * @param board  board
     */
    public void setBoard(ServerUtils server, Board board) {
        this.server = server;
        this.board = new ObservableObject<>(board);

        //Sort card lists
        Map<Long, CardList> m1 = new HashMap<>();
        board.getCardLists().sort(Comparator.comparing(CardList::getIndexInBoard));
        board.getCardLists().forEach(cl -> m1.put(cl.getId(), cl));

        Map<Long, Card> m2 = new HashMap<>();
        board.getCards().sort(Comparator.comparing(Card::getIndexInCardList));
        board.getCards().forEach(c -> m2.put(c.getId(), c));

        Map<Long, Tag> m3 = new HashMap<>();
        board.getTags().forEach(tag -> m3.put(tag.getId(), tag));

        Map<Long, Task> m4 = new HashMap<>();
        m2.forEach((aLong, card) -> {
            card.getTaskList().sort(Comparator.comparing(Task::getIndexInCard));
            card.getTaskList().forEach(task -> m4.put(task.getId(), task));
        });

        this.cardLists = new ObservableValueMap<>(m1);
        this.cards = new ObservableValueMap<>(m2);
        this.tags = new ObservableValueMap<>(m3);
        this.tasks = new ObservableValueMap<>(m4);

        listen();
    }

    /**
     * Listen for websocket messages
     */
    private void listen() {
        registerForBoard();
        registerForCardLists();
        registerForCards();
        registerForTags();
        registerForTasks();
    }

    /**
     * Registers for board
     */
    private void registerForBoard() {
        // Board
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId(),
            BoardDto.class,
            this::updateBoard
        );
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/delete",
            long.class,
            i -> {
                if(i != board.getValue().getId())
                    return;
                deleteBoard();
            }
        );
    }

    /**
     * Registers for cardlists
     */
    private void registerForCardLists() {
        // Card List
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/cardlists",
            CardListDto.class,
            this::updateCardList
        );
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/cardlists/delete",
            long.class,
            this::removeCardList
        );
    }

    /**
     * Registers for cards
     */
    private void registerForCards() {
        // Card
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/cards",
            CardDto.class,
            this::updateCard
        );
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/cards/delete",
            long.class,
            this::removeCard
        );
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/linkCardTag",
            CardTagDto.class,
            this::linkCardToTag
        );
    }

    /**
     * Register for tasks
     */
    private void registerForTasks() {
        //Task
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/tasks",
            TaskDto.class,
            this::updateTask
        );
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/tasks/delete",
            long.class,
            this::removeTask
        );
    }

    /**
     * Registers for tags
     */
    private void registerForTags() {
        // Tag
        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/tags",
            TagDto.class,
            this::updateTag
        );

        server.registerForMessages(
            "/out/boards/" + board.getValue().getId() + "/tags/delete",
            long.class,
            this::removeTag
        );
    }

    /**
     * Send value to destination and replace "boardId" when possible
     *
     * @param destination destination
     * @param value       value
     */
    public void send(String destination, Object value) {
        server.send(
            "/in" + destination.replaceAll("\\{boardId}", Long.toString(board.getValue().getId())),
            value
        );
    }

    /**
     * Listen to changes to the board
     *
     * @param listener listener
     */
    public void listenToBoard(Consumer<Board> listener) {
        board.addListener(observable -> listener.accept(board.getValue()));
    }

    /**
     * Update board from DTO
     *
     * @param dto Data Transfer Object
     */
    public void updateBoard(BoardDto dto) {
        dto.setId(board.getValue().getId());
        board.update(b -> BoardMapper.INSTANCE.partialUpdate(dto, b));
        cardLists.values().forEach(cl -> triggerCardList(cl.getId()));
    }

    /**
     * Delete the board
     */
    public void deleteBoard() {
        board.update(b -> null);
    }

    /**
     * Listen to changes to all card lists
     *
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToCardLists(MapChangeListener<CardList> listener) {
        return cardLists.listenToAll(listener);
    }

    /**
     * Listen to changes to a specific card list
     *
     * @param key      card list key
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToCardList(long key, MapChangeListener<CardList> listener) {
        return cardLists.listenTo(key, listener);
    }

    /**
     * Update card list from DTO
     *
     * @param dto Data Transfer Object
     */
    public synchronized void updateCardList(CardListDto dto) {
        dto.setCards(null);
        cardLists.trigger(
            dto.getId(),
            () -> CardListMapper.INSTANCE.toEntity(dto)
                .setBoard(board.getValue())
                .setCards(new ArrayList<>()),
            cardList -> CardListMapper.INSTANCE.partialUpdate(dto, cardList)
        );
    }

    /**
     * Trigger card list
     *
     * @param key key
     */
    public synchronized void triggerCardList(long key) {
        cardLists.trigger(key, cardLists.get(key), ChangeType.UPDATE);
    }

    /**
     * Remove card list by key
     *
     * @param key key
     */
    public synchronized void removeCardList(long key) {
        CardList cardList = cardLists.remove(key);
        if(cardList == null)
            return;

        // Remove references
        board.getValue().getCardLists().remove(cardList);
        board.getValue().getCards().removeAll(cardList.getCards());
        cardList.getCards().forEach(c -> cards.remove(c.getId()));
    }

    /**
     * Listen to changes to all cards
     *
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToCards(MapChangeListener<Card> listener) {
        return cards.listenToAll(listener);
    }

    /**
     * Listen to changes to a specific card
     *
     * @param key      card list key
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToCard(long key, MapChangeListener<Card> listener) {
        return cards.listenTo(key, listener);
    }

    /**
     * Triggers the card to be updated
     *
     * @param cardId id of the card
     */
    public synchronized void triggerCard(long cardId) {
        cards.trigger(cardId, cards.get(cardId), ChangeType.UPDATE);
    }

    /**
     * Update card from DTO
     *
     * @param dto Data Transfer Object
     */
    public synchronized void updateCard(CardDto dto) {
        cards.trigger(
            dto.getId(),
            () -> {
                CardList cardList = cardLists.get(dto.getCardListId());
                if(cardList == null)
                    throw new IllegalArgumentException("card list with key " + dto.getCardListId() + " does not exist");

                return CardMapper.INSTANCE.toEntity(dto).setCardList(cardList).setTags(new ArrayList<>());
            },
            card -> {
                CardList cardList = cardLists.get(dto.getCardListId());
                if(cardList == null)
                    throw new IllegalArgumentException("card list with key " + dto.getCardListId() + " does not exist");

                return CardMapper.INSTANCE.partialUpdate(dto, card.setCardList(cardList));
            }
        );
    }

    /**
     * Remove card by key
     *
     * @param key key
     */
    public synchronized void removeCard(long key) {
        Card card = cards.remove(key);
        if(card == null)
            return;

        // Remove references and fire listeners where needed
        board.getValue().getCards().remove(card);
        card.getCardList().getCards().remove(card);
        triggerCardList(card.getCardList().getId());
    }

    /**
     * Link/unlink card to/from tag
     *
     * @param dto card tag dto
     */
    public synchronized void linkCardToTag(CardTagDto dto) {
        Card card = cards.get(dto.getCardId());
        if(card == null)
            throw new IllegalArgumentException("Card with key " + dto.getCardId() + " does not exist");

        Tag tag = tags.get(dto.getTagId());
        if(tag == null)
            throw new IllegalArgumentException("Tag with key " + dto.getTagId() + " does not exist");

        if(dto.isLink()) {
            card.getTags().add(tag);
            tag.getCards().add(card);
        } else {
            card.getTags().remove(tag);
            tag.getCards().remove(card);
        }

        triggerCard(dto.getCardId());
        //Trigger tag
    }

    /**
     * Listen to changes to all tags
     *
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToTags(MapChangeListener<Tag> listener) {
        return tags.listenToAll(listener);
    }

    /**
     * Listen to changes to a specific tag
     *
     * @param key      tag key
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToTag(long key, MapChangeListener<Tag> listener) {
        return tags.listenTo(key, listener);
    }

    /**
     * Update tag from DTO
     *
     * @param dto Data Transfer Object
     */
    public synchronized void updateTag(TagDto dto) {
        tags.trigger(
            dto.getId(),
            () -> {
                Tag tag = TagMapper.INSTANCE.toEntity(dto)
                    .setBoard(board.getValue())
                    .setCards(new ArrayList<>());

                board.getValue().getTags().add(tag);
                return tag;
            },
            tag -> TagMapper.INSTANCE.partialUpdate(dto, tag)
        );
        tags.get(dto.getId()).getCards().forEach(card -> triggerCard(card.getId()));
    }

    /**
     * Remove tag by key
     *
     * @param key key
     */
    public synchronized void removeTag(long key) {
        Tag tag = tags.remove(key);
        if(tag == null)
            return;

        // Remove references and fire listeners where needed
        board.getValue().getTags().remove(tag);
        tag.getCards().forEach(card -> {
            card.getTags().remove(tag);
            triggerCard(card.getId());
        });
    }

    /**
     * Update card from DTO
     *
     * @param dto Data Transfer Object
     */
    public synchronized void updateTask(TaskDto dto) {
        tasks.trigger(
            dto.getId(),
            () -> {
                Card card = cards.get(dto.getCardId());
                if(card == null)
                    throw new IllegalArgumentException("Card with key " + dto.getCardId() + " does not exist");

                return TaskMapper.INSTANCE.toEntity(dto).setCard(card);
            },
            task -> TaskMapper.INSTANCE.partialUpdate(dto, task)
        );
    }

    /**
     * remove task by key
     *
     * @param key task key
     */
    public synchronized void removeTask(long key) {
        Task task = tasks.remove(key);
        if(task == null)
            return;

        // Remove references and fire listeners where needed
        task.getCard().getTaskList().remove(task);
        triggerCard(task.getCard().getId());
    }

    /**
     * Listen to changes to all tasks
     *
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToTasks(MapChangeListener<Task> listener) {
        return tasks.listenToAll(listener);
    }

    /**
     * Listen to changes to a specific task
     *
     * @param key      task key
     * @param listener listener
     * @return unregister listener
     */
    public Runnable listenToTask(long key, MapChangeListener<Task> listener) {
        return tasks.listenTo(key, listener);
    }

}
