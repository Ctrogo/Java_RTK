package DZ3_Object_String;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Для введенной с клавиатуры буквы английского алфавита
// нужно вывести слева стоящую букву на стандартной клавиатуре. При этом
// клавиатура замкнута, т.е. справа от буквы «p» стоит буква «a», а слева от "а"
// буква "р", также соседними считаются буквы «l» и буква «z», а буква «m» с буквой «q».
// Входные данные: строка входного потока содержит один символ —
// маленькую букву английского алфавита.
// Выходные данные: следует вывести букву стоящую слева от заданной
// буквы, с учетом замкнутости клавиатуры.

public class DZ3_Object_String_Z1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Повторный ввод при ошибке
        while (true) {
            System.out.print("Введите маленькую букву английского алфавита: ");
            input = scanner.nextLine();

            if (input.length() == 1 && input.charAt(0) >= 'a' && input.charAt(0) <= 'z') {
                break; // корректный ввод — выходим из цикла
            } else {
                System.out.println("Ошибка: введите одну маленькую английскую букву.");
            }
        }

        char letter = input.charAt(0);

        // вывести букву стоящую слева
        Map<Character, Character> leftNeighbor = new HashMap<>();
        String layout = "qwertyuiopasdfghjklzxcvbnm";

        for (int i = 0; i < layout.length(); i++) {
            char current = layout.charAt(i);
            char left = (i == 0) ? layout.charAt(layout.length() - 1) : layout.charAt(i - 1);
            leftNeighbor.put(current, left);
        }

        // учет замкнутости клавиатуры
        leftNeighbor.put('a', 'p');
        leftNeighbor.put('p', 'a');
        leftNeighbor.put('l', 'z');
        leftNeighbor.put('z', 'l');
        leftNeighbor.put('m', 'q');
        leftNeighbor.put('q', 'm');

        // Выходные данные
        char result = leftNeighbor.get(letter);
        System.out.printf("Для буквы \"%c\" слева на клавиатуре расположена буква \"%c\".%n", letter, result);

        scanner.close();
    }
}
