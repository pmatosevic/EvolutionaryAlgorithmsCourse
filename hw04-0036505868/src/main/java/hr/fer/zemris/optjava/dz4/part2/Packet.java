package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;

public class Packet {

    private List<Item> items;
    private int size = 0;

    public Packet() {
        items = new ArrayList<>();
    }

    public Packet(List<Item> items) {
        this.items = items;
        this.size = items.stream().mapToInt(Item::getSize).sum();
    }

    public List<Item> getItems() {
        return items;
    }

    public int getSize() {
        return size;
    }

    public Packet clone() {
        return new Packet(new ArrayList<>(items));
    }

    public void addItem(Item item) {
        items.add(item);
        size += item.getSize();
    }

    public void removeItem(Item item) {
        items.remove(item);
        size -= item.getSize();
    }

}
