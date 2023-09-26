package client.observable.interfaces;

import client.observable.enums.ChangeType;

public interface MapChangeListener<T> {

    /**
     * Listen to change
     *
     * @param value      current value
     * @param changeType type of the change
     */
    void listen(T value, ChangeType changeType);

}
