package client.observable;

import client.observable.enums.ChangeType;
import client.observable.interfaces.MapChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import lombok.Getter;

public class ObservableValueMap<K, V> {

    @Getter
    private final ObservableMap<K, V> map;
    private final Map<K, List<MapChangeListener<V>>> listeners;

    /**
     * Observable Value Map
     *
     * @param map underlying map
     */
    public ObservableValueMap(Map<K, V> map) {
        this.map = FXCollections.observableMap(map);
        this.listeners = new HashMap<>();

        this.map.addListener((javafx.collections.MapChangeListener<? super K, ? super V>) change -> {
            ChangeType changeType;
            if(change.wasRemoved()) {
                changeType = change.wasAdded() ? ChangeType.UPDATE : ChangeType.DELETE;
            } else {
                changeType = ChangeType.ADD;
            }

            V value = changeType != ChangeType.DELETE ? change.getValueAdded() : change.getValueRemoved();

            trigger(change.getKey(), value, changeType);

            if(changeType == ChangeType.DELETE)
                listeners.remove(change.getKey());
        });
    }

    /**
     * Listen to changes to all keys
     *
     * @param listener map change listener
     * @return unregister listener
     */
    public Runnable listenToAll(MapChangeListener<V> listener) {
        listeners.computeIfAbsent(
            null,
            key -> new ArrayList<>()
        ).add(listener);

        return () -> {
            List<MapChangeListener<V>> list = listeners.get(null);
            if(list == null)
                return;

            list.remove(listener);
        };
    }

    /**
     * Listen to changes to a specific key
     *
     * @param key      card list key
     * @param listener map change listener
     * @return unregister listener
     */
    public Runnable listenTo(K key, MapChangeListener<V> listener) {
        if(!map.containsKey(key))
            throw new IllegalArgumentException("card list with key " + key + " not found");

        listeners.computeIfAbsent(
            key,
            k -> new ArrayList<>()
        ).add(listener);

        return () -> {
            List<MapChangeListener<V>> list = listeners.get(key);
            if(list == null)
                return;

            list.remove(listener);
        };
    }

    /**
     * Add key with value, remap if already exists and remove if provided value is null
     *
     * @param key               key with which the resulting value is to be associated
     * @param supplier          supplier for the default value
     * @param remappingFunction the remapping function to recompute a value if present
     */
    public void trigger(K key, Supplier<V> supplier, Function<V, V> remappingFunction) {
        V current = map.get(key);
        if(current == null) {
            current = supplier.get();
            if(current != null)
                map.put(key, current);

            return;
        }

        V next = remappingFunction.apply(current);
        map.put(key, next);

        /*
        Manually trigger if "equal" since client.observable.ObservableValueMap ignores this case
        and our entity's equals only compares ID's rather than the full thing
         */
        if(!current.equals(next))
            return;

        trigger(key, next, ChangeType.UPDATE);
    }

    /**
     * Trigger update
     *
     * @param key        key
     * @param value      value
     * @param changeType type
     */
    public void trigger(K key, V value, ChangeType changeType) {
        // Generic Listeners
        listeners.getOrDefault(
            null,
            Collections.emptyList()
        ).forEach(l -> l.listen(value, changeType));

        // Listener for specific key
        listeners.getOrDefault(
            key,
            Collections.emptyList()
        ).forEach(l -> l.listen(value, changeType));
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return value
     */
    public V get(K key) {
        return map.get(key);
    }

    /**
     * Removes the mapping for a key from this map if it is present
     *
     * @param key whose mapping is to be removed from the map
     * @return previous value
     */
    public V remove(K key) {
        return map.remove(key);
    }

    /**
     * Get values
     *
     * @return collection of values
     */
    public Collection<V> values() {
        return map.values();
    }

}
