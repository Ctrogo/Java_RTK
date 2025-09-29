package com.example.dungeon.model;

import java.util.*;

/**
 * Room — комната игрового мира.
 *
 * Изменения:
 *  - добавлено поле doorRequirements: для направления -> имя ключа, который требуется для открытия двери.
 *  - методы isDoorLocked(direction), setDoorRequirement(direction, keyName), unlockDoor(direction)
 *  - describe() теперь помечает заблокированные выходы "(закрыто)".
 */
public class Room {
    private final String name;
    private final String description;
    private final Map<String, Room> neighbors = new HashMap<>();
    private final List<Item> items = new ArrayList<>();
    private Monster monster;

    // направление -> requiredKeyName (null / отсутствует значит не требуется)
    private final Map<String, String> doorRequirements = new HashMap<>();

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Map<String, Room> getNeighbors() {
        return neighbors;
    }

    public List<Item> getItems() {
        return items;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster m) {
        this.monster = m;
    }

    /**
     * Установить требование ключа для двери в указанном направлении.
     * Если keyName == null — дверь считается открытой.
     */
    public void setDoorRequirement(String direction, String keyName) {
        if (keyName == null) doorRequirements.remove(direction);
        else doorRequirements.put(direction, keyName);
    }

    /**
     * Проверка: требуется ли для данной двери ключ (и тем самым — заблокирована ли она)
     */
    public boolean isDoorLocked(String direction) {
        return doorRequirements.containsKey(direction);
    }

    /**
     * Получить имя ключа, требуемого для открытия двери в направлении direction.
     * Вернёт null, если ключ не требуется.
     */
    public String getRequiredKeyFor(String direction) {
        return doorRequirements.get(direction);
    }

    /**
     * Разблокировать дверь (удалить требование ключа).
     */
    public void unlockDoor(String direction) {
        doorRequirements.remove(direction);
    }

    public String describe() {
        StringBuilder sb = new StringBuilder(name + ": " + description);
        if (!items.isEmpty()) {
            sb.append("\nПредметы: ").append(String.join(", ", items.stream().map(Item::getName).toList()));
        }
        if (monster != null) {
            sb.append("\nВ комнате монстр: ").append(monster.getName()).append(" (ур. ").append(monster.getLevel()).append(")");
        }
        if (!neighbors.isEmpty()) {
            // помечаем закрытые выходы
            List<String> outs = new ArrayList<>();
            for (String dir : neighbors.keySet()) {
                String out = dir;
                if (isDoorLocked(dir)) out += " (закрыто)";
                outs.add(out);
            }
            sb.append("\nВыходы: ").append(String.join(", ", outs));
        }
        return sb.toString();
    }
}
