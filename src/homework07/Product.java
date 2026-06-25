package homework07;

import java.util.Objects;

// Класс Продукт (Product)
// Характеристики: название и стоимость.
// Ограничения: название не может быть пустым, не должно содержать только цифры,
// не короче 3 символов. Стоимость > 0.

public class Product {
    private String title;
    private int price;

    public Product(String title, int price) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }
        if (title.length() < 3) {
            throw new IllegalArgumentException("Название продукта не может быть короче 3 символов");
        }
        if (title.matches("\\d+")) {
            throw new IllegalArgumentException("Название продукта не может состоять только из цифр");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Стоимость продукта должна быть больше 0");
        }
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return title + " = " + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return price == product.price &&
                title.equalsIgnoreCase(product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase(), price);
    }
}
