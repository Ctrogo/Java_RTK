package DZ1_Hello_game;
// Задача 1: Составить программу вывода на экран в одну строку сообщения
//«Привет, имя_пользователя», где «имя_пользователя» - это введёное в консоль
//имя, для выполнения данного задания нужно использовать класс Scanner.
//Задача 2*. Вася и Петя играют в игру “Камень, ножницы, бумага”.
//Каждый из них показывает свою фигуру камень-0, ножницы-1, бумага-2.
//Программа определяет, кто из них выиграл.
//Выбор каждого участника формируется случайным образом


import java.util.Scanner;
import java.util.Random;

public class DZ1_Hello_game {
    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);

        // Задача 1: Приветствие
        System.out.print("Здравствуйте. Введите Ваше имя: ");
        String name = scan.nextLine();
        System.out.println("Здравствуй, " + name + "!");

        // Переход к 2 задаче
        System.out.print("\nПосмотрим на игру? (Да/Нет): ");
        String answer = scan.nextLine().trim();

        if (answer.equalsIgnoreCase("Да")) {
            playRockPaperScissors(scan);
        } else {
            System.out.println("Хорошо, до встречи!");
        }

        scan.close();
    }

    // Задача 2: Игра “Камень, ножницы, бумага”

    private static void playRockPaperScissors(Scanner scan) {
        Random random = new Random();

        int petya = random.nextInt(3); // Выбор Пети
        int vasya = random.nextInt(3); // Выбор Васи

        String[] choices = {"камень", "ножницы", "бумагу"};

        System.out.println("Петя выбрал " + choices[petya]);
        System.out.println("Вася выбрал " + choices[vasya]);

        if (petya == vasya) {
            System.out.println("Ничья!");
        } else if ((petya == 0 && vasya == 1) ||
                   (petya == 1 && vasya == 2) ||
                   (petya == 2 && vasya == 0)) {
            System.out.println("Победил Петя!");
        } else {
            System.out.println("Победил Вася!");
        }
    }
}