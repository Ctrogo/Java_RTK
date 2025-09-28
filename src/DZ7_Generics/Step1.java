package DZ7_Generics;

import java.util.*;

/**
 * Задача 1.
 * Реализовать метод, который на вход принимает ArrayList<T>,
 * а возвращает набор (Set) уникальных элементов.
 */
public class Step1 {

    public static <T> Set<T> getUniqueElements(ArrayList<T> list) {
        return new HashSet<>(list);
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<>();

        System.out.println("Задача 1: Уникальные элементы ArrayList");
        System.out.println("Введите элементы списка (каждый с новой строки через Enter). Для завершения введите END:");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("END")) break;
            list.add(input);
        }

        Set<String> unique = getUniqueElements(list);
        System.out.println("Набор уникальных элементов: " + unique);
    }
}
