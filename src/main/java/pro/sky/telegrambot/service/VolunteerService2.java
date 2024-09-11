package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.PetReport;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.VolunteerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class VolunteerService2 {

    private final Logger logger = LoggerFactory.getLogger(VolunteerService2.class);
    private final VolunteerRepository volunteerRepository;
    private final PetRepository petRepository;
    private final ClientRepository clientRepository;
    private final ReportRepository reportRepository;


    @Autowired
    public VolunteerService2(VolunteerRepository volunteerRepository, PetRepository petRepository, ClientRepository clientRepository, ReportRepository reportRepository) {
        this.volunteerRepository = volunteerRepository;
        this.petRepository = petRepository;
        this.clientRepository = clientRepository;
        this.reportRepository = reportRepository;
    }

    // Метод для проверки, зарегистрирован ли волонтер
    public boolean isVolunteerRegistered(Long chatId) {
        return volunteerRepository.findByChatId(chatId) != null;
    }

    // Метод для регистрации волонтера
    public Volunteer registerVolunteer(Long chatId, String name) {
        if (isVolunteerRegistered(chatId)) {
            return null;  // Возвращаем null, если волонтер уже зарегистрирован
        }

        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setName(name);
        return volunteerRepository.save(volunteer);
    }

    // Метод для добавления питомца волонтеру
    public Pet addPetToVolunteer(Long chatId, String name, String breed, String photoFileId) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        if (volunteer == null) {
            return null;
        }

        Pet pet = new Pet();
        pet.setName(name);
        pet.setBreed(breed);
        pet.setPhotoFileId(photoFileId);
        pet.setVolunteer(volunteer);
        return petRepository.save(pet);
    }

    public PetReport addPetReportToVolunteer(Long chatId, String name, String animalsDiet, String animalHealth, String animalHabit, String photoFileId) {
        PetReport petReport = new PetReport();
        petReport.setName(name);
        petReport.setAnimalsDiet(animalsDiet);
        petReport.setAnimalHealth(animalHealth);
        petReport.setAnimalHabits(animalHabit);
        petReport.setPhotoFileId(photoFileId);
        PetReport savedReport = reportRepository.save(petReport);
        logger.info("Saved pet report with ID {} for volunteer with chatId {}", savedReport.getId(), chatId);

        return savedReport;


    }

    // Метод для получения списка питомцев волонтера
    public List<Pet> getPetsByVolunteer(Long chatId) {
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        return volunteer != null ? volunteer.getPets() : List.of();
    }

    public List<PetReport> getReportByVolunteer(Long chatId) {
        return null;

    }


    public void deletePetReport(Long id) {
        reportRepository.deleteById(id);
    }

    // Метод для удаления питомца
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }

    public void deleteClientById(Long clientId) {
        clientRepository.deleteById(clientId);
        logger.info("Deleted client with ID: {}", clientId);
    }

    public boolean isClientPending(Long chatId) {
        return clientRepository.existsByChatId(chatId);
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
        logger.info("Saved client with ID: {}", client.getId());
    }

    public Client getClientByChatId(Long chatId) {
        return ClientRepository.findByChatId(chatId);
    }

    public void startReportProcess(Client client) {
        client.setReportCount(0); // Сбрасываем счетчик отчетов
        saveClient(client);
        logger.info("Started report process for client with ID: {}", client.getId());
    }

    public void updatePet(Pet pet) {
        pet.setVolunteer(null);
        petRepository.save(pet);
    }

    public PetReport getPetReportById(Long reportId) {
        return reportRepository.findById(reportId).orElse(null);
    }

    public List<Client> getClientsAwaitingReports() {
        return clientRepository.findAllClientsWithPositiveReportCounts(); // Логика фильтрации клиентов, ожидающих отчетов
    }


}
