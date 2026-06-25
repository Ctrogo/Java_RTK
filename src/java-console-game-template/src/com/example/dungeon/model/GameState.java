package com.example.dungeon.model;

import java.util.*;

/**
 * GameState — состояние игры:
 *  - игрок
 *  - текущая комната
 *  - карта всех комнат (реестр) — чтобы при загрузке можно было восстановить ссылки
 *  - visited — множество посещённых комнат (для команды map)
 *  - score
 */
public class GameState {
    private Player player;
    private Room current;
    private int score;

    // Реестр комнат по имени (для сериализации / восстановления)
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    // Посещённые комнаты
    private final Set<String> visited = new HashSet<>();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Room getCurrent() {
        return current;
    }

    public void setCurrent(Room r) {
        this.current = r;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int d) {
        this.score += d;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void registerRoom(Room r) {
        rooms.put(r.getName(), r);
    }

    public Room getRoomByName(String name) {
        return rooms.get(name);
    }

    public Set<String> getVisited() {
        return visited;
    }

    public void markVisited(String roomName) {
        visited.add(roomName);
    }

    public boolean wasVisited(String roomName) {
        return visited.contains(roomName);
    }
}
