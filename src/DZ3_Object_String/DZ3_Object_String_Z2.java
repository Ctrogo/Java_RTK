package DZ3_Object_String;

import java.util.Scanner;

// Задача 2. Задана последовательность, состоящая только из символов ‘>’,
//        ‘<’ и ‘-‘. Требуется найти количество стрел, которые спрятаны в этой
// последовательности. Стрелы – это подстроки вида ‘>>-->’ и ‘<--<<’.
// Входные данные: в первой строке входного потока записана строка,
// состоящая из символов ‘>’, ‘<’ и ‘-‘ (без пробелов). Строка может содержать до
// 106 символов.
// Выходные данные: в единственную строку выходного потока нужно
// вывести искомое количество стрелок.


public class DZ3_Object_String_Z2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите строку  из символов ‘>’, ‘<’ и ‘-‘ (без пробелов), до 106 символов: ");
        String input = scanner.nextLine();

        // Проверка длины
        if (input.length() > 106) {
            System.out.println("Ошибка: строка может содержать до 106 символов.");
            return;
        }

        // Проверка на допустимые символы
        for (char ch : input.toCharArray()) {
            if (ch != '>' && ch != '<' && ch != '-') {
                System.out.println("Ошибка: строка может содержать только символы '>', '<' и '-'.");
                return;
            }
        }

        String arrow1 = ">>-->";
        String arrow2 = "<--<<";
        int count = 0;

        for (int i = 0; i <= input.length() - 5; i++) {
            String sub = input.substring(i, i + 5);
            if (sub.equals(arrow1) || sub.equals(arrow2)) {
                count++;
            }
        }

        System.out.println("Количество стрел: " + count);
        scanner.close();
    }
}