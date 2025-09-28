package homework011.repository;

import homework011.model.Car;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileCarsRepository implements CarsRepository {
    private final List<Car> cars = new ArrayList<>();

    @Override
    public void loadFromFile(String path) throws IOException {
        cars.clear();
        List<String> lines = Files.readAllLines(Path.of(path));

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            if (parts.length != 5) continue;

            String number = parts[0].trim();
            String model = parts[1].trim();
            String color = parts[2].trim();
            long mileage = parseLongSafe(parts[3].trim());
            long cost = parseLongSafe(parts[4].trim());

            try {
                cars.add(new Car(number, model, color, mileage, cost));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при создании автомобиля: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    private long parseLongSafe(String s) {
        if (s == null) return 0L;
        String digits = s.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0L;
        return Long.parseLong(digits);
    }
}
