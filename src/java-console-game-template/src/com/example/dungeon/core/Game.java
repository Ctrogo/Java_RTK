package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Random random = new Random();

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
        });

        commands.put("gc", (ctx, a) -> {
            System.gc();
            System.out.println("Запрошена сборка мусора. Выполните gc-stats для просмотра.");
        });

        commands.put("alloc", (ctx, a) -> {
            if (a == null || a.isEmpty()) throw new InvalidCommandException("Укажите размер в MB: alloc <mb>");
            try {
                int mb = Integer.parseInt(a.get(0));
                byte[] block = new byte[mb * 1024 * 1024];
                block[0] = 1;
                System.out.println("Попытка выделить " + mb + " MB. Объект может быть собран GC.");
            } catch (NumberFormatException e) {
                throw new InvalidCommandException("Неверный формат числа: " + a.get(0));
            } catch (OutOfMemoryError e) {
                System.out.println("Ошибка: недостаточно памяти для " + a.get(0) + " MB.");
            }
        });

        commands.put("map", (ctx, a) -> {
            System.out.println("--- Карта мира ---");
            Map<String, Room> rooms = ctx.getRooms();
            String currentName = ctx.getCurrent() == null ? "" : ctx.getCurrent().getName();
            Set<String> visited = ctx.getVisited();
            rooms.forEach((name, room) -> {
                String mark = name.equals(currentName) ? " [ТЫ ЗДЕСЬ]" :
                        visited.contains(name) ? " [посещена]" : "";
                System.out.println(name + mark);
            });
            System.out.println("------------------");
        });

        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));

        // === move ===
        commands.put("move", (ctx, a) -> {
            if (a == null || a.isEmpty()) {
                throw new InvalidCommandException("Укажите направление: move <north|south|east|west>");
            }
            String direction = a.get(0).toLowerCase(Locale.ROOT).trim();
            Room current = ctx.getCurrent();
            if (current == null) throw new InvalidCommandException("Текущее положение неизвестно.");

            if (current.isDoorLocked(direction)) {
                String requiredKey = current.getRequiredKeyFor(direction);
                throw new InvalidCommandException("Дверь '" + direction + "' закрыта. Требуется ключ: " + requiredKey);
            }

            Room next = current.getNeighbors().get(direction);
            if (next == null) {
                throw new InvalidCommandException("Нет пути '" + direction + "' из " + current.getName());
            }
            ctx.setCurrent(next);
            ctx.markVisited(next.getName());
            System.out.println("Вы перешли в: " + next.getName());
            System.out.println(next.describe());
        });

        // === take ===
        commands.put("take", (ctx, a) -> {
            if (a == null || a.isEmpty()) throw new InvalidCommandException("Укажите название предмета: take <item name>");
            String wanted = String.join(" ", a).trim();
            Room current = ctx.getCurrent();
            Optional<Item> found = current.getItems().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(wanted))
                    .findFirst();
            if (found.isEmpty()) throw new InvalidCommandException("Нет такого предмета в комнате: " + wanted);
            Item item = found.get();
            current.getItems().remove(item);
            ctx.getPlayer().getInventory().add(item);
            System.out.println("Взято: " + item.getName());
        });

        // === inventory ===
        commands.put("inventory", (ctx, a) -> {
            Player p = ctx.getPlayer();
            List<Item> inv = p.getInventory();
            if (inv.isEmpty()) {
                System.out.println("Инвентарь пуст.");
                return;
            }
            Map<String, List<Item>> groups = inv.stream()
                    .collect(Collectors.groupingBy(i -> i.getClass().getSimpleName()));
            groups.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String type = entry.getKey();
                        List<Item> items = entry.getValue();
                        String names = items.stream().map(Item::getName).collect(Collectors.joining(", "));
                        System.out.println("- " + type + " (" + items.size() + "): " + names);
                    });
        });

        // === use ===
        commands.put("use", (ctx, a) -> {
            if (a == null || a.isEmpty()) throw new InvalidCommandException("Укажите название предмета: use <item name>");
            String name = String.join(" ", a).trim();
            Player p = ctx.getPlayer();
            Optional<Item> found = p.getInventory().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(name))
                    .findFirst();
            if (found.isEmpty()) throw new InvalidCommandException("У вас нет: " + name);
            Item item = found.get();
            item.apply(ctx);
        });

        // === fight ===
        commands.put("fight", (ctx, a) -> {
            Room room = ctx.getCurrent();
            Monster m = room.getMonster();
            if (m == null) throw new InvalidCommandException("В комнате нет монстра для боя.");
            Player p = ctx.getPlayer();
            System.out.println("Начинается бой с " + m.getName() + " (ур. " + m.getLevel() + ", HP " + m.getHp() + ")");

            while (p.getHp() > 0 && m.getHp() > 0) {
                int dmgToMonster = p.getAttack();
                m.setHp(m.getHp() - dmgToMonster);
                System.out.println("Вы бьёте " + m.getName() + " на " + dmgToMonster + ". HP монстра: " + Math.max(0, m.getHp()));
                if (m.getHp() <= 0) {
                    System.out.println("Монстр " + m.getName() + " повержен!");
                    Item drop = generateLootFor(m);
                    if (drop != null) {
                        room.getItems().add(drop);
                        System.out.println("Выпало: " + drop.getName());
                    }
                    room.setMonster(null);
                    break;
                }

                int dmgToPlayer = Math.max(1, m.getLevel());
                p.setHp(p.getHp() - dmgToPlayer);
                System.out.println("Монстр отвечает на " + dmgToPlayer + ". Ваше HP: " + Math.max(0, p.getHp()));
                if (p.getHp() <= 0) {
                    System.out.println("Вы погибли. Игра завершена.");
                    System.exit(0);
                }
            }
        });

        commands.put("save", (ctx, a) -> SaveLoad.save(ctx));
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока!");
            System.exit(0);
        });
    }

    private Item generateLootFor(Monster m) {
        if (m.getLevel() <= 1) {
            return new Potion("Зелье победы от " + m.getName(), 5);
        } else if (m.getLevel() == 2) {
            return new Weapon("Коготь " + m.getName(), 2);
        } else {
            return new Key("Ключ от Сокровищницы");
        }
    }

    /**
     * Построение игрового мира:
     * - Площадь → Лес (волк + зелье)
     * - Лес → Пещера
     * - Пещера → Замок (монстр-охранник)
     * - Замок → Сокровищница (дверь закрыта ключом)
     */
    private void bootstrapWorld() {
        Player hero = new Player("Герой", 20, 5);
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        Room castle = new Room("Замок", "Древний замок, в его стенах что-то шевелится.");
        Room treasury = new Room("Сокровищница", "Сокровищница: редкие зелья и трофеи.");

        // связи
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);

        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);

        cave.getNeighbors().put("east", castle);
        castle.getNeighbors().put("west", cave);

        castle.getNeighbors().put("north", treasury);
        castle.setDoorRequirement("north", "Ключ от Сокровищницы");
        treasury.getNeighbors().put("south", castle);

        // предметы и монстры
        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 1, 10));

        castle.setMonster(new Monster("Охранник замка", 3, 20));

        treasury.getItems().add(new Potion("Зелье могущества", 10));
        treasury.getItems().add(new Potion("Зелье силы", 8));
        treasury.getItems().add(new Weapon("Трофейный клинок", 1));

        state.registerRoom(square);
        state.registerRoom(forest);
        state.registerRoom(cave);
        state.registerRoom(castle);
        state.registerRoom(treasury);

        state.setCurrent(square);
        state.markVisited(square.getName());
    }

    public void run() {
        System.out.println("DungeonMini (TEMPLATE). 'help' — команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                List<String> parts = Arrays.asList(line.split("\\s+"));
                String cmd = parts.get(0).toLowerCase(Locale.ROOT);
                List<String> args = parts.size() > 1 ? parts.subList(1, parts.size()) : Collections.emptyList();
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}
