package homework011.repository;

import homework011.model.Car;

import java.io.IOException;
import java.util.List;

public interface CarsRepository {
    void loadFromFile(String path) throws IOException;
    List<Car> getAllCars();
}
