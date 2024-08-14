package pro.sky.telegrambot.repository;

import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.model.Shelter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с приютами.
 */
@Repository
public class ShelterRepository {

    private final List<Shelter> shelters = Arrays.asList(
            new Shelter("Cat Shelter", "Address for Cat Shelter", AnimalType.CAT),
            new Shelter("Dog Shelter", "Address for Dog Shelter", AnimalType.DOG)
    );

    /**
     * Находит приют по типу животных.
     * @param type тип животных
     * @return опционально найденный приют
     */
    public Optional<Shelter> findByType(AnimalType type) {
        return shelters.stream()
                .filter(shelter -> shelter.getType() == type)
                .findFirst();
    }
}
