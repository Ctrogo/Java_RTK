package attestation01;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Класс Покупатель (Person)
// Характеристики Покупателя: имя, сумма денег и пакет с продуктами (массив объектов типа Продукт).
// Имя не может быть пустой строкой и не может быть короче 3 символов.
// Деньги не могут быть отрицательным числом.


public class Person {
    private String name;
    private int balance;
    private List<Product> cart;

    public Person(String name, int balance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательным");
        }

        this.name = name;
        this.balance = balance;
        this.cart = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public List<Product> getCart() {
        return cart;
    }

    public void addToCart(Product product) {
        if (product.getPrice() > this.balance) {
            System.out.println(this.name + " не может позволить себе " + product.getTitle());
        } else {
            this.balance -= product.getPrice();
            this.cart.add(product);
            System.out.println(this.name + " купил(а) " + product.getTitle());
        }
    }

    @Override
    public String toString() {
        if (cart.isEmpty()) {
            return name + " - Ничего не куплено";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" - ");
            for (int i = 0; i < cart.size(); i++) {
                sb.append(cart.get(i).getTitle());
                if (i != cart.size() - 1) sb.append(", ");
            }
            return sb.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return name.equalsIgnoreCase(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
