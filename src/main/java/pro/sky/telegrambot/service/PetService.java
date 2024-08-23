package pro.sky.telegrambot.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.BotException;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.UserRepository;

import java.util.List;

@Service
public class PetService {

    private final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }


    public Pet addPet(Pet pet) {
        logger.info("Питомец добавлен");
        return petRepository.save(pet);
    }

    public Pet getPetById(long id) {
        try {
            logger.info("Питомец был найден, id =" + id);
            return petRepository.findById(id).orElseThrow(BotException::new);
        } catch (BotException e) {
            logger.error("Питомец с id =" + id + " не найден");
            throw e;
        }
    }

    public Pet updatePet(Pet pet) {
        logger.info("Данные о питомце обновлены");
        return petRepository.findById(pet.getId())
                .map(entity -> petRepository.save(pet))
                .orElse(null);
    }

    public boolean deletePet(long id) {
        logger.info("Питомец с id = {} был удален" + id);
        return petRepository.findById(id)
                .map(entity ->{
                    petRepository.delete(entity);
                    return true;
                }).orElse(false);
    }

    public List<Pet> getAll() {
        return petRepository.findAll();
    }


    public void adopt(long petId, long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if(user==null || user.getPet()!=null) {
            throw new RuntimeException();
        }
        var pet = petRepository.findById(petId).orElse(null);
        if(pet==null) {
            throw new RuntimeException();
        }
        user.setPet(pet);
        userRepository.save(user);

    }
}
