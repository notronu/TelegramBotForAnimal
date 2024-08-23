package pro.sky.telegrambot.controller;



import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.service.PetService;

@RestController
@RequestMapping(path = "/pets")
@Api(tags = "API для работы с питомцами")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @ApiOperation(value = "Добавить нового питомца", notes = "Создает нового питомца")
    public Pet addPet(@RequestBody Pet pet) {
        return petService.addPet(pet);
    }

    @GetMapping
    @ApiOperation(value = "Получить питомца по ID", notes = "Возвращает питомца по его ID")
    @ApiParam(name = "id", value = "ID питомца", required = true)
    public Pet getPetById(@RequestParam long id) {
        return petService.getPetById(id);
    }

    @PutMapping
    @ApiOperation(value = "Обновить информацию о питомце", notes = "Обновляет существующего питомца")
    public Pet updatePet(@RequestBody Pet pet) {
        return petService.updatePet(pet);
    }

    @DeleteMapping
    @ApiOperation(value = "Удалить питомца", notes = "Удаляет питомца по его ID")
    @ApiParam(name = "id", value = "ID питомца", required = true)
    public boolean deletePet(@RequestParam long id) {
        return petService.deletePet(id);
    }

    @GetMapping("adopt")
    @ApiOperation(value = "Усыновить питомца", notes = "Усыновляет питомца для пользователя")
    @ApiParam(name = "petId", value = "ID питомца для усыновления", required = true)
    @ApiParam(name = "userId", value = "ID пользователя, усыновляющего питомца", required = true)
    public void adopt(@RequestParam long petId, @RequestParam long userId) {
        petService.adopt(petId, userId);
    }
}
