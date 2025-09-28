package DZ7_Generics;

import java.util.Scanner;

/**
 * Запуск задачи 2: проверка анаграммы через класс AnagramChecker.
 */
public class Step2 {
    public static void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Задача 2: Проверка валидности анаграмм");

        System.out.print("Введите первое слово или фразу: ");
        String s = scanner.nextLine();

        System.out.print("Введите второе слово или фразу: ");
        String t = scanner.nextLine();

        // Проверка через класс AnagramChecker
        Step2_1 checker = new Step2_1(s, t);
        System.out.println("Результат порверки: " + checker.isAnagram());

        // Проверка через объект AnagramData (альтернативный способ)
        Step2_2 data = new Step2_2("бейсбол", "бобслей");
        System.out.println("Проверка: Результат для 'бейсбол' и 'бобслей': " + data.check());
    }
}
