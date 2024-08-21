package pro.sky.telegrambot.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    public Pet addPet(@RequestBody Pet pet) {
        return petService.addPet(pet);
    }


    public Pet getPetById(@RequestParam long id) {
        return petService.getPetById(id);
    }


    public Pet updatePet(@RequestBody Pet pet) {
        return petService.updatePet(pet);
    }


    public boolean deletePet(@RequestParam long id) {
        return petService.deletePet(id);
    }


    public void adopt(@RequestParam long petId, @RequestParam long userId) {
        petService.adopt(petId, userId);
    }
}
