package client.observable;

import java.util.function.Function;
import javafx.beans.value.ObservableValueBase;

public class ObservableObject<T> extends ObservableValueBase<T> {

    private T obj;

    /**
     * Observable Object
     *
     * @param obj start value
     */
    public ObservableObject(T obj) {
        this.obj = obj;
    }

    /**
     * Get value of object
     *
     * @return value
     */
    @Override
    public T getValue() {
        return obj;
    }

    /**
     * Update the object
     *
     * @param update function to update the object
     */
    public void update(Function<T, T> update) {
        obj = update.apply(obj);
        fireValueChangedEvent();// Fire event
    }

}
