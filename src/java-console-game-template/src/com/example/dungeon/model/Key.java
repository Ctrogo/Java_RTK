package com.example.dungeon.model;

import java.util.ArrayList;

/**
 * Ключ — предмет, который открывает двери.
 */
public class Key extends Item {
    public Key(String name) {
        super(name);
    }

    /**
     * При применении ключа — пытаемся открыть дверь в текущей комнате,
     * требующую именно этот ключ. Если такая дверь найдена — она разблокируется.
     * Ключ удаляется из инвентаря (использован).
     */
    @Override
    public void apply(GameState ctx) {
        Room current = ctx.getCurrent();
        Player p = ctx.getPlayer();
        String myName = getName();

        // ищем направления в текущей комнате, которые требуют этот ключ
        boolean opened = false;
        for (String dir : new ArrayList<>(current.getNeighbors().keySet())) {
            String required = current.getRequiredKeyFor(dir);
            if (required != null && required.equalsIgnoreCase(myName)) {
                current.unlockDoor(dir);
                opened = true;
                System.out.println("Ключ " + myName + " подошёл. Дверь в направлении '" + dir + "' открыта.");
                // удаляем ключ из инвентаря (использован)
                p.getInventory().remove(this);
                break; // открываем только одну дверь за применение
            }
        }

        if (!opened) {
            System.out.println("Ключ " + myName + " здесь не подходит.");
        }
    }
}
