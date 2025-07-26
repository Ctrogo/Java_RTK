package DZ2_Televizor;

// Задача 2: «Классы: поля, свойства, методы»
//Реализовать класс Телевизор. У класса есть поля, свойства и методы.
//Проверить работу в классе App, методе main.
// 1. Создан класс Телевизор;
//2. У класса есть поля, свойства и методы. Поля желательно сделать private. Задать новые значения полям класса можно через конструктор.
//4. Создан класс App с методом main.
//5. В методе main класса App создано несколько экземпляров класса Телевизор.
//6. Дополнительно. Задавать параметры класса Телевизор с клавиатуры или случайным числом


public class Televizor {
    // Поля (характеристики телевизора)
    private String marka;               // Марка
    private String model;               // Модель
    private int diagonal;              // Диагональ экрана в дюймах
    private String razreshenie;        // Разрешение экрана
    private int chastotaObnovleniya;   // Частота обновления
    private String tipPodsvetki;       // Тип подсветки экрана

    // Конструктор — задаёт все поля при создании объекта
    public Televizor(String marka, String model, int diagonal, String razreshenie,
                     int chastotaObnovleniya, String tipPodsvetki) {
        this.marka = marka;
        this.model = model;
        this.diagonal = diagonal;
        this.razreshenie = razreshenie;
        this.chastotaObnovleniya = chastotaObnovleniya;
        this.tipPodsvetki = tipPodsvetki;
    }

    // Метод — поведение телевизора: вывод его характеристик
    public void print() {
        System.out.println("Телевизор " + marka + " " + model);
        System.out.println("Диагональ: " + diagonal + "\"");
        System.out.println("Разрешение: " + razreshenie);
        System.out.println("Частота обновления: " + chastotaObnovleniya + " Гц");
        System.out.println("Тип подсветки: " + tipPodsvetki);
        System.out.println(); // Пустая строка для отделения
    }
}
