package de.stupidus.util.list;

import java.util.Iterator;

public class DynamicList<T> implements Iterable<T> {
    private final ListPool pool;
    private final int startIndex;
    private final int capacity;
    private int size;

    public DynamicList(ListPool pool, int startIndex, int capacity) {
        this.pool = pool;
        this.startIndex = startIndex;
        this.capacity = capacity;
        this.size = 0;
    }

    public void add(T value) {
        if (size >= capacity)
            throw new IllegalStateException("Liste voll (dieses Segment kann nicht wachsen).");
        pool.set(startIndex + size, value);
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return (T) pool.get(startIndex + index);
    }

    public void set(int index, T value) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        pool.set(startIndex + index, value);
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            pool.set(startIndex + i, null);
        }
        size = 0;
    }

    public boolean contains(T value) {
        for (int i = 0; i < size; i++)  {
            if (value.equals(pool.get(startIndex + i))) {
                return true;
            }
        }
        return false;
    }

    // Gibt diesen Speicherbereich komplett zurück
    public void removeList() {
        pool.free(startIndex, capacity);
        size = 0;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                return (T) pool.get(startIndex + cursor++);
            }
        };
    }
}
