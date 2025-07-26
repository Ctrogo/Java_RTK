package DZ3_Object_String;

import java.util.Arrays;
import java.util.Scanner;

// Задача 3*. Задана строка, состоящая из букв английского алфавита,
// разделенных одним пробелом. Необходимо каждую последовательность
// символов упорядочить по возрастанию и вывести слова в нижнем регистре.
// Входные данные: в единственной строке последовательность символов
// представляющее два слова.
// Выходные данные: упорядоченные по возрастанию буквы в нижнем регистре.


public class DZ3_Object_String_Z3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        String[] words;

        while (true) {
            System.out.print("Введите последовательность английских символов, разделенных одним пробелом: ");
            input = scanner.nextLine().toLowerCase();
            words = input.split(" ");

            if (words.length != 2) {
                System.out.println("Ошибка: нужно ввести ровно два слова, разделённых одним пробелом.\n");
                continue;
            }

            boolean valid = true;
            for (String word : words) {
                if (!word.matches("[a-zA-Z]+")) {
                    System.out.println("Ошибка: слова должны содержать только английские буквы.\n");
                    valid = false;
                    break;
                }
            }

            if (valid) break;
        }


        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            System.out.println(new String(chars));
        }

        scanner.close();
    }
}