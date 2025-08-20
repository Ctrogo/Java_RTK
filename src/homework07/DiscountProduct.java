package homework07;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Скидочный продукт — наследуется от Product
public class DiscountProduct extends Product {
    private int discount;   // размер скидки в %
    private int days;       // срок действия скидки (в днях)
    private LocalDate endDate; // дата окончания скидки

    public DiscountProduct(String title, int price, int discount, int days) {
        super(title, price);

        if (discount <= 0 || discount >= 100) {
            throw new IllegalArgumentException("Скидка должна быть >0 и <100%");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("Срок действия скидки должен быть положительным");
        }

        this.discount = discount;
        this.days = days;
        this.endDate = LocalDate.now().plusDays(days);
    }

    @Override
    public int getPrice() {
        // цена со скидкой
        return super.getPrice() - (super.getPrice() * discount / 100);
    }

    public int getDiscount() {
        return discount;
    }

    public int getDays() {
        return days;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Новый метод для App.java
    public String getDiscountInfo() {
        return String.format("Скидка %d%% на продукт \"%s\" сроком %d дней действует до %s",
                discount,
                getTitle(),
                days,
                endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Override
    public String toString() {
        return getTitle() + " = " + getPrice() +
                " (скидка " + discount + "%, до " +
                endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountProduct)) return false;
        if (!super.equals(o)) return false;
        DiscountProduct that = (DiscountProduct) o;
        return discount == that.discount && days == that.days && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discount, days, endDate);
    }
}
