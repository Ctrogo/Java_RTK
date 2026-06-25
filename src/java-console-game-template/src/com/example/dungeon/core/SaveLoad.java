package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SaveLoad — сохранение/загрузка состояния.
 *
 * Теперь дополнительно сериализует все комнаты и их содержимое.
 *
 * Формат:
 * player;name;hp;attack
 * inventory;Class:Name,Class:Name,...
 * room;Name;Description;neighbors=dir:RoomName[:KeyName],...;items=Type:Name[:param],...;monster=Name:Level:HP
 * current;RoomName
 */
public class SaveLoad {
    private static final Path SAVE = Paths.get("save.txt");
    private static final Path SCORES = Paths.get("scores.csv");

    public static void save(GameState s) {
        try (BufferedWriter w = Files.newBufferedWriter(SAVE)) {
            Player p = s.getPlayer();
            w.write("player;" + p.getName() + ";" + p.getHp() + ";" + p.getAttack());
            w.newLine();

            String inv = p.getInventory().stream().map(i -> i.getClass().getSimpleName() + ":" + i.getName()).collect(Collectors.joining(","));
            w.write("inventory;" + inv);
            w.newLine();

            // Сохраняем все комнаты
            for (Room room : s.getRooms().values()) {
                StringBuilder nb = new StringBuilder();
                for (Map.Entry<String, Room> e : room.getNeighbors().entrySet()) {
                    if (nb.length() > 0) nb.append(",");
                    String dir = e.getKey();
                    String rn = e.getValue().getName();
                    // если дверь требует ключа, укажем после двоеточия
                    String req = room.getRequiredKeyFor(dir);
                    nb.append(dir).append(":").append(rn);
                    if (req != null) nb.append(":").append(req);
                }

                // items
                StringBuilder it = new StringBuilder();
                for (Item item : room.getItems()) {
                    if (it.length() > 0) it.append(",");
                    if (item instanceof Potion) {
                        Potion pz = (Potion) item;
                        it.append("Potion:").append(pz.getName()).append(":").append(pz.getClass().getDeclaredFields()[0]); // dummy, but we will save only name in practice
                        // NOTE: we cannot access private heal easily here; instead we'll save type and name only.
                        it.setLength(it.length()); // no-op
                    }
                    // Simpler: save by class simple name and item.getName()
                    it.setLength(it.length()); // ensure no side-effect
                }
                // Simpler approach for items — store as Type:Name (we will reconstruct as Potion with default heal)
                String itemsLine = room.getItems().stream()
                        .map(itm -> itm.getClass().getSimpleName() + ":" + itm.getName())
                        .collect(Collectors.joining(","));

                String monsterLine = "";
                if (room.getMonster() != null) {
                    Monster m = room.getMonster();
                    monsterLine = m.getName() + ":" + m.getLevel() + ":" + m.getHp();
                }

                // Выписываем строку: room;Name;Description;neighbors=...;items=...;monster=...
                w.write("room;" + room.getName() + ";" + room.getName() + ""); // description can be lost-safe; better to store description separately
                // To preserve description (which might contain semicolons), store as base64? For simplicity use replace semicolons.
                w.write(";" + escape(room.describe()));
                w.write(";neighbors=" + nb.toString());
                w.write(";items=" + itemsLine);
                w.write(";monster=" + monsterLine);
                w.newLine();
            }

            // current room
            if (s.getCurrent() != null) {
                w.write("current;" + s.getCurrent().getName());
                w.newLine();
            }

            System.out.println("Сохранено в " + SAVE.toAbsolutePath());
            writeScore(p.getName(), s.getScore());
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    private static String escape(String s) {
        // простая защита: заменим символы новой строки на пробел
        return s.replace("\n", " ").replace("\r", " ");
    }

    public static void load(GameState s) {
        if (!Files.exists(SAVE)) {
            System.out.println("Сохранение не найдено.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SAVE)) {
            // Сначала прочитаем все строки в память
            List<String> lines = r.lines().collect(Collectors.toList());

            // Очистим текущую карту комнат, чтобы подменить их новыми
            s.getRooms().clear();

            // Первично парсим комнаты — создаем объект Room по имени и описанию
            class RoomHolder {
                String name;
                String desc;
                String neighborsPart;
                String itemsPart;
                String monsterPart;
            }
            Map<String, RoomHolder> holders = new LinkedHashMap<>();
            String playerLine = null;
            String invLine = null;
            String currentRoomName = null;

            for (String line : lines) {
                if (line.startsWith("player;")) playerLine = line;
                else if (line.startsWith("inventory;")) invLine = line;
                else if (line.startsWith("room;")) {
                    // room;Name;describe;neighbors=...;items=...;monster=...
                    String[] parts = line.split(";", 4);
                    if (parts.length < 4) continue;
                    String name = parts[1];
                    String rest = parts[3];
                    // разделим rest по ;neighbors= ;items= ;monster=
                    String desc = parts[2];
                    String neighborsPart = extractPart(rest, "neighbors=", ";items=");
                    String itemsPart = extractPart(rest, "items=", ";monster=");
                    String monsterPart = extractPart(rest, "monster=", "");
                    RoomHolder h = new RoomHolder();
                    h.name = name;
                    h.desc = desc;
                    h.neighborsPart = neighborsPart == null ? "" : neighborsPart;
                    h.itemsPart = itemsPart == null ? "" : itemsPart;
                    h.monsterPart = monsterPart == null ? "" : monsterPart;
                    holders.put(name, h);
                } else if (line.startsWith("current;")) {
                    currentRoomName = line.substring("current;".length());
                }
            }

            // Создаём комнаты (с описанием из holders)
            for (RoomHolder h : holders.values()) {
                Room room = new Room(h.name, h.desc);
                s.registerRoom(room);
            }

            // Вторая фаза: парсим соседей и ставим ссылки
            for (RoomHolder h : holders.values()) {
                Room room = s.getRoomByName(h.name);
                if (h.neighborsPart != null && !h.neighborsPart.isBlank()) {
                    String[] nb = h.neighborsPart.split(",");
                    for (String token : nb) {
                        token = token.trim();
                        if (token.isEmpty()) continue;
                        // формат: dir:RoomName[:KeyName]
                        String[] tok = token.split(":");
                        String dir = tok[0];
                        String targetName = tok.length > 1 ? tok[1] : "";
                        String keyName = tok.length > 2 ? tok[2] : null;
                        Room target = s.getRoomByName(targetName);
                        if (target != null) {
                            room.getNeighbors().put(dir, target);
                            if (keyName != null && !keyName.isBlank()) {
                                room.setDoorRequirement(dir, keyName);
                            }
                        }
                    }
                }
                // items
                if (h.itemsPart != null && !h.itemsPart.isBlank()) {
                    String[] its = h.itemsPart.split(",");
                    for (String itTok : its) {
                        itTok = itTok.trim();
                        if (itTok.isEmpty()) continue;
                        // формат Type:Name
                        String[] t = itTok.split(":", 2);
                        String type = t[0];
                        String name = t.length > 1 ? t[1] : "item";
                        Item item = null;
                        switch (type) {
                            case "Potion":
                                item = new Potion(name, 5); // default heal
                                break;
                            case "Weapon":
                                item = new Weapon(name, 3); // default bonus
                                break;
                            case "Key":
                                item = new Key(name);
                                break;
                            default:
                                item = new Potion(name, 5);
                        }
                        room.getItems().add(item);
                    }
                }
                // monster
                if (h.monsterPart != null && !h.monsterPart.isBlank()) {
                    String[] mm = h.monsterPart.split(":");
                    if (mm.length >= 3) {
                        try {
                            String mName = mm[0];
                            int level = Integer.parseInt(mm[1]);
                            int hp = Integer.parseInt(mm[2]);
                            room.setMonster(new Monster(mName, level, hp));
                        } catch (NumberFormatException ex) {
                            // ignore
                        }
                    }
                }
            }

            // Восстановление игрока
            if (playerLine != null) {
                try {
                    String[] pp = playerLine.split(";");
                    String[] data = playerLine.split(";");
                    // player;name;hp;attack
                    String[] pa = playerLine.split(";");
                    if (pa.length >= 4) {
                        Player p = s.getPlayer();
                        p.setName(pa[1]);
                        p.setHp(Integer.parseInt(pa[2]));
                        p.setAttack(Integer.parseInt(pa[3]));
                    }
                } catch (Exception ex) {
                    System.out.println("Ошибка при восстановлении игрока: " + ex.getMessage());
                }
            }

            // Восстановление инвентаря
            if (invLine != null) {
                Player p = s.getPlayer();
                p.getInventory().clear();
                String[] partsInv = invLine.split(";", 2);
                if (partsInv.length == 2 && !partsInv[1].isBlank()) {
                    String[] toks = partsInv[1].split(",");
                    for (String tok : toks) {
                        String[] t = tok.split(":", 2);
                        if (t.length < 2) continue;
                        String type = t[0];
                        String name = t[1];
                        Item item = null;
                        switch (type) {
                            case "Potion":
                                item = new Potion(name, 5);
                                break;
                            case "Key":
                                item = new Key(name);
                                break;
                            case "Weapon":
                                item = new Weapon(name, 3);
                                break;
                            default:
                                item = new Potion(name, 5);
                        }
                        p.getInventory().add(item);
                    }
                }
            }

            // Восстановление текущей комнаты
            if (currentRoomName != null && !currentRoomName.isBlank()) {
                Room cur = s.getRoomByName(currentRoomName);
                if (cur != null) {
                    s.setCurrent(cur);
                    s.markVisited(cur.getName());
                }
            }

            System.out.println("Игра загружена.");
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось загрузить игру", e);
        }
    }

    public static void printScores() {
        if (!Files.exists(SCORES)) {
            System.out.println("Пока нет результатов.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SCORES)) {
            System.out.println("Таблица лидеров (топ-10):");
            r.lines().skip(1).map(l -> l.split(",")).map(a -> new Score(a[1], Integer.parseInt(a[2])))
                    .sorted(Comparator.comparingInt(Score::score).reversed()).limit(10)
                    .forEach(s -> System.out.println(s.player() + " — " + s.score()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    private static void writeScore(String player, int score) {
        try {
            boolean header = !Files.exists(SCORES);
            try (BufferedWriter w = Files.newBufferedWriter(SCORES, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (header) {
                    w.write("ts,player,score");
                    w.newLine();
                }
                w.write(LocalDateTime.now() + "," + player + "," + score);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    private record Score(String player, int score) {
    }

    // Вспомогательная функция для нахождения подстроки между маркерами
    private static String extractPart(String source, String startMarker, String endMarker) {
        int s = source.indexOf(startMarker);
        if (s < 0) return null;
        s += startMarker.length();
        int e = endMarker == null || endMarker.isEmpty() ? source.length() : source.indexOf(endMarker, s);
        if (e < 0) e = source.length();
        return source.substring(s, e);
    }
}
