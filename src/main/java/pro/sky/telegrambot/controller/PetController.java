package pro.sky.telegrambot.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

@RestController
@RequestMapping(path = "/pets")
@Tag(name = "Pets API", description = "API для управления питомцами")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @Operation(summary = "Добавить нового питомца", description = "Создает нового питомца")
    public Pet addPet(@RequestBody Pet pet) {
        return petService.addPet(pet);
    }

    @GetMapping
    @Operation(summary = "Получение питомца по айди", description = "Получает питомца по айди.")
    @Parameter(name = "id", description = "ID питомца", required = true)
    public Pet getPetById(@RequestParam long id) {
        return petService.getPetById(id);
    }

    @PutMapping
    @Operation(summary = "Обновляет информацию о питомце", description = "Обновляет питомца")
    public Pet updatePet(@RequestBody Pet pet) {
        return petService.updatePet(pet);
    }

    @DeleteMapping
    @Operation(summary = "УДаляет питомца", description = "Удаляет питомца по айди")
    @Parameter(name = "id", description = "ID питомца", required = true)
    public boolean deletePet(@RequestParam long id) {
        return petService.deletePet(id);
    }

    @GetMapping("adopt")
    @Operation(summary = "Усыновление", description = "Усыновление питомца")
    @Parameter(name = "petId", description = "ID питомца", required = true)
    @Parameter(name = "userId", description = "ID пользователя", required = true)
    public void adopt(@RequestParam long petId, @RequestParam long userId) {
        petService.adopt(petId, userId);
    }
}
