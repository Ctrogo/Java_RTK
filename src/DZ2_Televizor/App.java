package DZ2_Televizor;
// 5. В методе main класса App создано несколько экземпляров класса Телевизор.
public class App {
    public static void main(String[] args) {
        // Создание экземпляров класса Televizor с конкретными характеристиками
        Televizor tv1 = new Televizor("Haier", "55 Smart TV AX Pro", 55, "3840x2160", 60, "Direct LED");
        Televizor tv2 = new Televizor("Samsung", "UE75DU8000UXRU", 75, "3840x2160", 60, "Edge LED");
        Televizor tv3 = new Televizor("Sber", "SDX-43U4128", 43, "3840x2160", 60, "Direct LED");
        Televizor tv4 = new Televizor("Витязь", "32LH0221", 32, "1366x768", 60, "LED");

        // Вывод информации о каждом телевизоре
        tv1.print();
        tv2.print();
        tv3.print();
        tv4.print();
    }
}
