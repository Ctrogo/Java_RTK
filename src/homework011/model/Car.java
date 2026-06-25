package homework011.model;

import java.util.Objects;

/**
 * Модель Автомобиль.
 * Поля: number, model, color, mileage, cost
 * Геттеры/сеттеры с минимальной валидацией.
 */
public class Car {
    private String number;   // Номер автомобиля
    private String model;    // Модель
    private String color;    // Цвет
    private long mileage;    // Пробег (>= 0)
    private long cost;       // Стоимость (>= 0)

    public Car(String number, String model, String color, long mileage, long cost) {
        setNumber(number);
        setModel(model);
        setColor(color);
        setMileage(mileage);
        setCost(cost);
    }

    public String getNumber() {
        return number;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public long getMileage() {
        return mileage;
    }

    public long getCost() {
        return cost;
    }

    public void setNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Номер автомобиля не может быть пустым");
        }
        this.number = number.trim();
    }

    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Модель не может быть пустой");
        }
        this.model = model.trim();
    }

    public void setColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Цвет не может быть пустым");
        }
        this.color = color.trim();
    }

    public void setMileage(long mileage) {
        if (mileage < 0) throw new IllegalArgumentException("Пробег не может быть отрицательным");
        this.mileage = mileage;
    }

    public void setCost(long cost) {
        if (cost < 0) throw new IllegalArgumentException("Стоимость не может быть отрицательной");
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-9s %-7s %7d %10d",
                number, model, color, mileage, cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return number.equalsIgnoreCase(car.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number.toLowerCase());
    }
}
