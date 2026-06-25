package DZ7_Generics;

import java.util.Arrays;

/**
 * Класс AnagramChecker.
 * Хранит две строки и проверяет, являются ли они анаграммами.
 */
public class Step2_1 {
    private String s;
    private String t;

    public Step2_1(String s, String t) {
        this.s = s;
        this.t = t;
    }

    // Проверка анаграммы
    public boolean isAnagram() {
        if (s.length() != t.length()) return false;

        char[] arr1 = s.toLowerCase().toCharArray();
        char[] arr2 = t.toLowerCase().toCharArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        return Arrays.equals(arr1, arr2);
    }
}
