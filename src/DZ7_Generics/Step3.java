package DZ7_Generics;

import java.util.*;

/**
 * Запуск задачи 3 через класс PowerfulSet.
 */
public class Step3 {
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        Step3_1 ps = new Step3_1();

        System.out.println("Задача 3: Операции с множествами");

        System.out.println("Введите элементы множества set1. Для завершения END:");
        Set<String> set1 = new HashSet<>();
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("END")) break;
            set1.add(input);
        }

        System.out.println("Введите элементы множества set2. Для завершения END:");
        Set<String> set2 = new HashSet<>();
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("END")) break;
            set2.add(input);
        }

        System.out.println("Пересечение двух наборов: " + ps.intersection(set1, set2));
        System.out.println("Объединение двух наборов: " + ps.union(set1, set2));
        System.out.println("Разность двух наборов: " + ps.relativeComplement(set1, set2));
    }
}
