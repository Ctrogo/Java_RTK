package attestation01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Создать в классе App метод main и проверить работу приложения.
//Данные Покупателей и Продукты вводятся с клавиатуры, для считывания
//данных потребуется использовать класс Scanner. Продукты в цикле выбираются
//покупателями по очереди и, пока не введено слово END, наполняется пакет.
//Обработать следующие ситуации:
//а. Если покупатель не может позволить себе продукт, то напечатайте
//соответствующее сообщение ("[Имя человека] не может позволить себе
//[Название продукта]").
//б. Если ничего не куплено, выведите имя человека, за которым
//следует "Ничего не куплено".
//в. В случае неверного ввода - сообщение: "Деньги не могут быть
//отрицательными", пустого имени - сообщение: "Имя не может быть
//пустым" или длина имени менее 3 символов – сообщение: "Имя не может быть короче 3 символов".

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> peopleList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        System.out.print("Показать тестовые данные? (Да/Нет): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("да")) {
            // Тестовые данные: продукты
            productList.add(new Product("Хлеб", 40));
            productList.add(new Product("Молоко", 60));
            productList.add(new Product("Торт", 1000));
            productList.add(new Product("Кофе растворимый", 879));
            productList.add(new Product("Масло", 150));
            productList.add(new Product("Мороженое", 200));

            // Тестовые данные: покупатели
            try {
                peopleList.add(new Person("Павел Андреевич", 10000));
                peopleList.add(new Person("Анна Петровна", 2000));
                peopleList.add(new Person("Борис", 10));
                peopleList.add(new Person("Женя", 0));
                peopleList.add(new Person("Света", -3));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при создании покупателя (Света, -3): " + e.getMessage());
            }

            try {
                peopleList.add(new Person("Фа", 100));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при создании покупателя (Фа, 100): " + e.getMessage());
            }

            // Тестовые данные: покупки
            for (Person person : peopleList) {
                switch (person.getName()) {
                    case "Павел Андреевич":
                        person.addToCart(findProductByTitle(productList, "Хлеб"));
                        person.addToCart(findProductByTitle(productList, "Масло"));
                        person.addToCart(findProductByTitle(productList, "Торт"));
                        break;
                    case "Анна Петровна":
                        person.addToCart(findProductByTitle(productList, "Кофе растворимый"));
                        person.addToCart(findProductByTitle(productList, "Молоко"));
                        person.addToCart(findProductByTitle(productList, "Молоко"));
                        person.addToCart(findProductByTitle(productList, "Молоко"));
                        person.addToCart(findProductByTitle(productList, "Торт"));
                        break;
                    case "Борис":
                        person.addToCart(findProductByTitle(productList, "Торт"));
                        break;
                    case "Женя":
                        person.addToCart(findProductByTitle(productList, "Мороженое"));
                        break;
                }
            }

        } else {

            System.out.println("Введите продукты (название=цена). Для завершения — END");
            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("END")) break;

                String[] parts = line.split("=");
                if (parts.length != 2) {
                    System.out.println("Ошибка ввода. Используйте формат: Название=Цена");
                    continue;
                }

                String title = parts[0].trim();
                int price;
                try {
                    price = Integer.parseInt(parts[1].trim());
                    productList.add(new Product(title, price));
                } catch (NumberFormatException e) {
                    System.out.println("Цена должна быть числом.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            System.out.println("Введите покупателей (Имя=Сумма). Для завершения — END");
            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("END")) break;

                String[] parts = line.split("=");
                if (parts.length != 2) {
                    System.out.println("Ошибка ввода. Используйте формат: Имя=Сумма");
                    continue;
                }

                String name = parts[0].trim();
                int balance;
                try {
                    balance = Integer.parseInt(parts[1].trim());
                    peopleList.add(new Person(name, balance));
                } catch (NumberFormatException e) {
                    System.out.println("Сумма должна быть числом.");
                } catch (IllegalArgumentException e) {
                    System.out.printf("Ошибка при создании покупателя (%s, %s): %s%n", name, parts[1].trim(), e.getMessage());
                }
            }

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
                    Product selected = findProductByTitle(productList, input);
                    if (selected != null) {
                        person.addToCart(selected);
                    } else {
                        System.out.println("Такого продукта нет.");
                    }
                }
            }
        }

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
