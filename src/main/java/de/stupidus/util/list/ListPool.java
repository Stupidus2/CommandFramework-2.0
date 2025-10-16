package de.stupidus.util.list;


import java.util.ArrayList;
import java.util.List;

public class ListPool {

    private final List<Object[]> objectList = new ArrayList<>();
    private final List<Integer> freePages = new ArrayList<>();
    private final int pageSize;

    private static ListPool instance;

    // Initialize ListPool

    public ListPool(int pageSize) {
        this.pageSize = pageSize;
        instance = this;
    }

    public ListPool() {
        this(128);
    }

    public void allocate() {

    }

    public void addPage() {

    }

    // Getter
    public static ListPool getInstance() {
        return instance;
    }
}