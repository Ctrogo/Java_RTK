package homework011;

import homework011.model.Car;
import homework011.repository.CarsRepository;
import homework011.repository.FileCarsRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    // Путь к файлу — кроссплатформенно формируем относительный путь к src/homework011/data/cars.txt
    private static final String DATA_FILE = Paths.get("src", "homework011", "data", "cars.txt").toString();

    public static void main(String[] args) {
        CarsRepository repo = new FileCarsRepository();

        // Загружаем данные из файла
        try {
            repo.loadFromFile(DATA_FILE);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        List<Car> cars = repo.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("Список автомобилей пуст. Проверьте файл: " + DATA_FILE);
            return;
        }

        // Печать базы
        System.out.println("Автомобили в базе:");
        System.out.printf("%-10s %-10s %-10s %10s %12s%n", "Number", "Model", "Color", "Mileage", "Cost");
        cars.forEach(c -> System.out.printf("%-10s %-10s %-10s %10d %12d%n",
                c.getNumber(), c.getModel(), c.getColor(), c.getMileage(), c.getCost()));

        Scanner scanner = new Scanner(System.in);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(new Locale("ru"));
        DecimalFormat df = new DecimalFormat("#0.00", dfs);

        // Меню (повторяется, пока не введут END)
        while (true) {
            System.out.println();
            System.out.println("Используя Java Stream API выводим (введите номер пункта 1, 2, 3 или 4, для выхода END):");
            System.out.println("1) Номера всех автомобилей, имеющих заданный в переменной цвет colorToFind или нулевой пробег mileageToFind.");
            System.out.println("2) Количество уникальных моделей в ценовом диапазоне от n до m.");
            System.out.println("3) Вывести цвет автомобиля с минимальной стоимостью.");
            System.out.println("4) Среднюю стоимость искомой модели modelToFind.");
            System.out.print("Ваш выбор (1/2/3/4/END): ");

            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("END")) break;

            switch (choice) {
                case "1": {
                    // Пункт 1: номера авто по цвету или пробегу
                    Set<String> colors = cars.stream()
                            .map(Car::getColor)
                            .map(String::toLowerCase)
                            .collect(Collectors.toCollection(TreeSet::new));
                    System.out.println("Доступные цвета: " + colors);
                    System.out.print("Укажите цвет автомобиля (введите точное имя цвета): ");
                    String colorToFind = scanner.nextLine().trim();

                    System.out.print("Укажите пробег для поиска (например 0): ");
                    String mileageInput = scanner.nextLine().trim();
                    long mileageToFind = parseInputLong(mileageInput);

                    List<String> numbers = cars.stream()
                            .filter(c -> c.getColor().equalsIgnoreCase(colorToFind) || c.getMileage() == mileageToFind)
                            .map(Car::getNumber)
                            .collect(Collectors.toList());

                    if (numbers.isEmpty()) {
                        System.out.println("Нет автомобилей с указанным цветом или пробегом.");
                    } else {
                        System.out.println("Номера автомобилей по цвету или пробегу: " + String.join(" ", numbers));
                    }
                }
                break;

                case "2": {
                    // Пункт 2: количество уникальных моделей в диапазоне цен
                    long minPrice = cars.stream().mapToLong(Car::getCost).min().orElse(0L);
                    long maxPrice = cars.stream().mapToLong(Car::getCost).max().orElse(0L);
                    System.out.printf("Доступный диапазон цен: от %d до %d%n", minPrice, maxPrice);

                    System.out.print("Введите нижнюю границу n: ");
                    long n = parseInputLong(scanner.nextLine().trim());
                    System.out.print("Введите верхнюю границу m: ");
                    long m = parseInputLong(scanner.nextLine().trim());

                    // делаем локальные final-значения для потоков (они effectively final здесь)
                    long finalN = n;
                    long finalM = m;

                    long uniqueModelsCount = cars.stream()
                            .filter(c -> c.getCost() >= finalN && c.getCost() <= finalM)
                            .map(Car::getModel)
                            .map(String::toLowerCase)
                            .distinct()
                            .count();

                    System.out.println("Уникальные автомобили: " + uniqueModelsCount + " шт.");
                }
                break;

                case "3": {
                    // Пункт 3: цвет автомобиля с минимальной стоимостью
                    cars.stream()
                            .min(Comparator.comparingLong(Car::getCost))
                            .ifPresentOrElse(
                                    c -> System.out.println("Цвет автомобиля с минимальной стоимостью: " + c.getColor()),
                                    () -> System.out.println("Список автомобилей пуст.")
                            );
                }
                break;

                case "4": {
                    // Пункт 4: средняя стоимость модели
                    Set<String> models = cars.stream()
                            .map(Car::getModel)
                            .collect(Collectors.toCollection(TreeSet::new));
                    System.out.println("Доступные модели: " + models);
                    System.out.print("Введите модель для вычисления средней стоимости: ");
                    String modelToFind = scanner.nextLine().trim();

                    double avg = cars.stream()
                            .filter(c -> c.getModel().equalsIgnoreCase(modelToFind))
                            .mapToLong(Car::getCost)
                            .average()
                            .orElse(0.0);

                    System.out.printf("Средняя стоимость модели %s: %s%n", modelToFind, df.format(avg));
                }
                break;

                default:
                    System.out.println("Неверный выбор. Введите 1, 2, 3, 4 или END.");
            } // switch
        } // while

        scanner.close();
        System.out.println("Работа завершена.");
    }

    // Утилита: парсинг числа, убираем все не-цифровые символы
    private static long parseInputLong(String s) {
        if (s == null) return 0L;
        String digits = s.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0L;
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
