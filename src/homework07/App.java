package homework07;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> peopleList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        // --- Выбор режима ---
        String choice = "";
        while (choice.isEmpty()) {
            System.out.print("Показать тестовые данные? (Да/Нет): ");
            choice = scanner.nextLine().trim().toLowerCase();
        }

        if (choice.equals("да")) {
            // --- Тестовые продукты ---
            productList.add(new Product("Хлеб", 40));
            productList.add(new Product("Молоко", 60));
            productList.add(new Product("Торт", 1000));
            productList.add(new Product("Кофе растворимый", 879));
            productList.add(new Product("Масло", 150));
            productList.add(new Product("Мороженое", 200));
            productList.add(new DiscountProduct("Сок", 100, 20, 15)); // скидочный тест

            // --- Тестовые покупатели ---
            try {
                peopleList.add(new Person("Павел Андреевич", 10000));
                peopleList.add(new Person("Анна Петровна", 2000));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

        } else {
            // --- Ввод обычных продуктов ---
            System.out.println("Введите продукты (Название=Цена). Для завершения — END");
            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split("=");
                if (parts.length != 2) {
                    System.out.println("Ошибка ввода. Используйте формат: Название=Цена");
                    continue;
                }

                String title = parts[0].trim();
                try {
                    int price = Integer.parseInt(parts[1].trim());
                    productList.add(new Product(title, price));
                } catch (NumberFormatException e) {
                    System.out.println("Цена должна быть числом.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            // --- Ввод скидочных продуктов ---
            System.out.println("Введите скидочные продукты (Название=Цена;Скидка%;Дней). Для завершения — END");
            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("END")) break;
                if (line.isEmpty()) continue;

                try {
                    String[] parts = line.split("=");
                    if (parts.length != 2) {
                        System.out.println("Ошибка формата. Пример: Молоко=60;10;20");
                        continue;
                    }

                    String title = parts[0].trim();
                    String[] rest = parts[1].split(";");
                    if (rest.length != 3) {
                        System.out.println("Ошибка формата. Пример: Молоко=60;10;20");
                        continue;
                    }

                    int price = Integer.parseInt(rest[0].trim());
                    int discount = Integer.parseInt(rest[1].trim());
                    int days = Integer.parseInt(rest[2].trim());

                    DiscountProduct dp = new DiscountProduct(title, price, discount, days);
                    productList.add(dp);
                    System.out.println(dp.getDiscountInfo());

                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            // --- Ввод покупателей ---
            System.out.println("Введите покупателей (Имя=Сумма). Для завершения — END");
            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split("=");
                if (parts.length != 2) {
                    System.out.println("Ошибка ввода. Используйте формат: Имя=Сумма");
                    continue;
                }

                String name = parts[0].trim();
                try {
                    int balance = Integer.parseInt(parts[1].trim());
                    peopleList.add(new Person(name, balance));
                } catch (NumberFormatException e) {
                    System.out.println("Сумма должна быть числом.");
                } catch (IllegalArgumentException e) {
                    System.out.printf("Ошибка при создании покупателя (%s, %s): %s%n",
                            name, parts[1].trim(), e.getMessage());
                }
            }

            // --- Выбор продуктов для покупателей ---
            for (Person person : peopleList) {
                System.out.println("Покупатель: " + person.getName());
                System.out.println("Доступные продукты:");
                for (Product product : productList) {
                    System.out.println(" - " + product);
                }
                System.out.println("Введите название продукта для покупки или END:");
                while (true) {
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("END")) break;
                    if (input.isEmpty()) continue;

                    Product selected = findProductByTitle(productList, input);
                    if (selected != null) {
                        person.addToCart(selected);
                    } else {
                        System.out.println("Такого продукта нет.");
                    }
                }
            }
        }

        // --- Итог ---
        System.out.println("\n--- Результаты покупок ---");
        for (Person person : peopleList) {
            if (person.getCart().isEmpty()) {
                System.out.printf("%s - Ничего не куплено. Недостаточно средств. Текущий баланс: %d%n",
                        person.getName(), person.getBalance());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(person.getName()).append(" - ");
                for (int i = 0; i < person.getCart().size(); i++) {
                    sb.append(person.getCart().get(i).getTitle());
                    if (i < person.getCart().size() - 1) sb.append(", ");
                }
                sb.append(". Текущий баланс: ").append(person.getBalance());
                System.out.println(sb);
            }
        }

        scanner.close();
    }

    private static Product findProductByTitle(List<Product> list, String title) {
        for (Product product : list) {
            if (product.getTitle().equalsIgnoreCase(title)) return product;
        }
        return null;
    }
}
