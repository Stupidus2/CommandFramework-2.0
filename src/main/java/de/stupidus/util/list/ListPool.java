package de.stupidus.util.list;

import java.util.ArrayList;
import java.util.List;

public class ListPool {
    private Object[] data;
    private final List<int[]> freeRanges = new ArrayList<>();
    private int nextFreeIndex = 0;

    public ListPool(int initialCapacity) {
        this.data = new Object[initialCapacity];
    }

    // Automatische Erstellung einer Liste mit Auto-Index
    public synchronized <T> DynamicList<T> createList(Class<T> type, int capacity) {
        int start = allocate(capacity);
        return new DynamicList<>(this, start, capacity);
    }

    private synchronized int allocate(int length) {
        // Prüfe, ob es einen freien Bereich gibt
        for (int i = 0; i < freeRanges.size(); i++) {
            int[] range = freeRanges.get(i);
            if (range[1] >= length) {
                freeRanges.remove(i);
                return range[0];
            }
        }

        // Wenn kein freier Bereich, dann erweitern
        if (nextFreeIndex + length >= data.length) {
            expand(Math.max(data.length * 2, nextFreeIndex + length));
        }

        int start = nextFreeIndex;
        nextFreeIndex += length;
        return start;
    }

    // Gibt Bereich frei z.B. bei removeList
    protected synchronized void free(int start, int length) {
        freeRanges.add(new int[]{start, length});
        for (int i = start; i < start + length && i < data.length; i++) {
            data[i] = null;
        }
    }

    private void expand(int newSize) {
        Object[] newData = new Object[newSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    protected Object get(int index) {
        return data[index];
    }

    protected void set(int index, Object value) {
        data[index] = value;
    }

    public int capacity() {
        return data.length;
    }

    public int used() {
        return nextFreeIndex;
    }
}