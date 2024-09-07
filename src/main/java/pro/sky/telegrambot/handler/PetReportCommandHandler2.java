package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.PetReport;
import pro.sky.telegrambot.service.VolunteerService2;

import java.util.HashMap;
import java.util.Map;

@Component
public class PetReportCommandHandler2 implements CommandHandler2 {

    private final Logger logger = LoggerFactory.getLogger(PetReportCommandHandler2.class);
    private final VolunteerService2 volunteerService2;
    private final TelegramBot telegramBot;
    private final PetReport petReport;

    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, PetReport> tempPetReportData = new HashMap<>();

    @Autowired
    public PetReportCommandHandler2(VolunteerService2 volunteerService2, TelegramBot telegramBot, PetReport petReport) {
        this.volunteerService2 = volunteerService2;
        this.telegramBot = telegramBot;
        this.petReport = petReport;
    }

    @Override
    public boolean canHandle(String command) {
        return command !=null && command.equals("Отправить отчет") ||
                !userStates.isEmpty();
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();

        if (text != null && text.equals("Отправить отчет")) {
            startPetReport(chatId);
        } else if (userStates.containsKey(chatId) && "AWAITING_PET_NAME".equals(userStates.get(chatId))) {
            handlePetName(chatId, text);
        } else if (userStates.containsKey(chatId) && "AWAITING_ANIMALS_DIET".equals(userStates.get(chatId))) {
            handlePetsDiet(chatId, text);
        } else if (userStates.containsKey(chatId) && "AWAITING_ANIMAL_HEALTH".equals(userStates.get(chatId))) {
            handlePetHealth(chatId, text);
        } else if (userStates.containsKey(chatId) && "AWAITING_ANIMAL_HABITS".equals(userStates.get(chatId))) {
            handlePetHabits(chatId, text);
        } else if (userStates.containsKey(chatId) && "AWAITING_PET_PHOTO".equals(userStates.get(chatId))) {
            handlePetPhoto(chatId, message);
        }
    }

    @Override
    public void handleCallbackQuery(CallbackQuery callbackQuery) {

    }

    private void startPetReport(Long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Пожалуйста, отправьте кличку питомца."));
        userStates.put(chatId, "AWAITING_PET_NAME");
    }

    private void handlePetName(Long chatId, String petName) {
        PetReport tempPetReport = new PetReport();
        tempPetReport.setName(petName.trim());
        tempPetReportData.put(chatId, tempPetReport);

        telegramBot.execute(new SendMessage(chatId, "Пожалуйста, опишите рацион питомца."));
        userStates.put(chatId, "AWAITING_ANIMALS_DIET");
    }

    private void handlePetsDiet(Long chatId, String animalsDiet) {
        PetReport tempPetReport = tempPetReportData.get(chatId);
        tempPetReport.setAnimalsDiet(animalsDiet.trim());

        telegramBot.execute(new SendMessage(chatId, "Пожалуйста, опишите самочувствие питомца."));
        userStates.put(chatId, "AWAITING_ANIMAL_HEALTH");
    }

    private void handlePetHealth(Long chatId, String animalHealth) {
        PetReport tempPetReport = tempPetReportData.get(chatId);
        tempPetReport.setAnimalHealth(animalHealth.trim());

        telegramBot.execute(new SendMessage(chatId, "Пожалуйста, опишите новые привычки питомца."));
        userStates.put(chatId, "AWAITING_PET_PHOTO");
    }

    private void handlePetHabits(Long chatId, String animalsDiet) {
        PetReport tempPetReport = tempPetReportData.get(chatId);
        tempPetReport.setAnimalsDiet(animalsDiet.trim());

        telegramBot.execute(new SendMessage(chatId, "Пожалуйста, отправьте фото питомца."));
        userStates.put(chatId, "AWAITING_PET_PHOTO");
    }

    private void handlePetPhoto(Long chatId, Message message) {
        if (message.photo() != null && message.photo().length > 0) {
            PhotoSize photo = message.photo()[message.photo().length - 1];
            String fileId = photo.fileId();

            PetReport tempPetReport = tempPetReportData.get(chatId);
            tempPetReport.setPhotoFileId(fileId);

            // Сохраняем питомца
            volunteerService2.addPetReportToVolunteer(chatId, tempPetReport.getName(), tempPetReport.getAnimalsDiet(), tempPetReport.getAnimalHealth(), tempPetReport.getAnimalHabits(), tempPetReport.getPhotoFileId());

            telegramBot.execute(new SendMessage(chatId, "Отчет успешно добавлен!"));

            // Очищаем состояние и временные данные
            userStates.remove(chatId);
            tempPetReportData.remove(chatId);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Пожалуйста, отправьте корректное фото питомца."));
        }
    }
}
