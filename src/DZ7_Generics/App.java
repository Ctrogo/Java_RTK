package DZ7_Generics;

import java.util.Scanner;

/**
 * Главное меню для запуска задач 1, 2 и 3.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Меню ---");
            System.out.println("1 - Задача 1 (Generics: уникальные элементы в ArrayList)");
            System.out.println("2 - Задача 2 (Класс AnagramChecker: проверка валидной анаграммы)");
            System.out.println("3 - Задача 3 (Класс PowerfulSet: операции над множествами)");
            System.out.println("END - завершить работу");
            System.out.print("Введите номер задачи или END: ");

            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("END")) {
                System.out.println("Программа завершена.");
                break;
            }

            switch (choice) {
                case "1":
                    Step1.run();
                    break;
                case "2":
                    Step2.run();
                    break;
                case "3":
                    Step3.run();
                    break;
                default:
                    System.out.println("Ошибка: введите 1, 2, 3 или END.");
            }
        }

        scanner.close();
    }
}
