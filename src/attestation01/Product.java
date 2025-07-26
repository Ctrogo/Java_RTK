package attestation01;

import java.util.Objects;

// Класс Продукт (Product)
// Характеристики: название и стоимость. Название продукта не может быть пустой строкой.
// Стоимость продукта не может быть отрицательным числом.
// В классах переопределены методы toString(), equals(), hashcode().

public class Product {
    private String title;
    private int price;

    public Product(String title, int price) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустой строкой");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть отрицательным числом");
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
