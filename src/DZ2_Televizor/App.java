package DZ2_Televizor;

// 5. В методе main класса App создано несколько экземпляров класса Телевизор.
// 6. Дополнительно. Задавать параметры класса Телевизор с клавиатуры

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Televizor tv1 = new Televizor("Haier", "55 Smart TV AX Pro", 55, "3840x2160", 60, "Direct LED");
        Televizor tv2 = new Televizor("Samsung", "UE75DU8000UXRU", 75, "3840x2160", 60, "Edge LED");
        Televizor tv3 = new Televizor("Sber", "SDX-43U4128", 43, "3840x2160", 60, "Direct LED");
        Televizor tv4 = new Televizor("Витязь", "32LH0221", 32, "1366x768", 60, "LED");

        Scanner scanner = new Scanner(System.in);
        System.out.print("У нас уже создано несколько телевизоров. Показать их? (Да/Нет): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("да")) {
            System.out.println("\nСписок доступных телевизоров:\n");
            tv1.print();
            tv2.print();
            tv3.print();
            tv4.print();
        } else {

            System.out.println("\nВведите параметры нового телевизора:");

            System.out.print("Марка: ");
            String marka = scanner.nextLine();

            System.out.print("Модель: ");
            String model = scanner.nextLine();

            int diagonal;
            while (true) {
                System.out.print("Диагональ (в дюймах): ");
                if (scanner.hasNextInt()) {
                    diagonal = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Пожалуйста, введите число.");
                    scanner.nextLine();
                }
            }

            System.out.print("Разрешение: ");
            String razreshenie = scanner.nextLine();

            int chastota;
            while (true) {
                System.out.print("Частота обновления (Гц): ");
                if (scanner.hasNextInt()) {
                    chastota = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Пожалуйста, введите число.");
                    scanner.nextLine();
                }
            }

            System.out.print("Тип подсветки: ");
            String podsvetka = scanner.nextLine();

            Televizor userTV = new Televizor(marka, model, diagonal, razreshenie, chastota, podsvetka);

            System.out.println("\nВаш телевизор:");
            userTV.print();
        }

        scanner.close();
    }
}
