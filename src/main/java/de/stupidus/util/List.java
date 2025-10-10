package de.stupidus.util;

public class List<K> {


    public void add(K key) {

    }

    static class Node<K> {
        K key;
        int index;
        Node<K> last;
        Node<K> next;
        public Node(K key, int index) {
            this.key = key;
            this.index = index;
        }
    }
}
