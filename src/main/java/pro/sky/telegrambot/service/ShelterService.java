package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.ShelterRepository;

import java.util.Optional;

/**
 * Сервис для работы с приютами.
 */
@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public Optional<Shelter> findShelterByType(AnimalType type) {
        return shelterRepository.findByType(type);
    }

}
