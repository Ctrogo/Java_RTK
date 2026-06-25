package DZ7_Generics;

import java.util.Arrays;

/**
 * Класс AnagramData.
 * Альтернативный способ: данные передаются сразу в объект.
 */
public class Step2_2 {
    private String first;
    private String second;

    public Step2_2(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public boolean check() {
        if (first.length() != second.length()) return false;

        char[] arr1 = first.toLowerCase().toCharArray();
        char[] arr2 = second.toLowerCase().toCharArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        return Arrays.equals(arr1, arr2);
    }
}
