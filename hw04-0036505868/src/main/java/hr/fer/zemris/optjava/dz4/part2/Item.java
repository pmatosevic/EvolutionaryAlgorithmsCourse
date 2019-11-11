package hr.fer.zemris.optjava.dz4.part2;

import java.util.Objects;

public class Item implements Comparable<Item> {

    private int id;
    private int size;

    public Item(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(o.size, this.size);
    }
}
