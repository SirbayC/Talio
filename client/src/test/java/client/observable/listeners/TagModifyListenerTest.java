package client.observable.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.observable.enums.ChangeType;
import commons.entities.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

class TagModifyListenerTest {

    private TagModifyListener listener;

    @Test
    public void testAddTag() {
        ObservableList<Tag> list = FXCollections.observableArrayList();
        TagModifyListener listener = new TagModifyListener(list);
        Tag tag = new Tag().setName("test");

        listener.listen(tag, ChangeType.ADD);

        assertTrue(list.contains(tag));
        assertEquals(1, list.size());
    }

    @Test
    public void testListenDelete() {
        ObservableList<Tag> list = FXCollections.observableArrayList();
        listener = new TagModifyListener(list);
        Tag tag = new Tag().setName("tag");
        list.add(tag);
        listener.listen(tag, ChangeType.DELETE);
        assertFalse(list.contains(tag));
        assertEquals(0, list.size());
    }

    @Test
    public void testListenUpdate() {
        ObservableList<Tag> list = FXCollections.observableArrayList();
        TagModifyListener listener = new TagModifyListener(list);
        Tag tag = new Tag().setName("tag");
        listener.listen(tag, ChangeType.UPDATE);
        assertFalse(list.contains(tag));
        assertEquals(0, list.size());
    }

}