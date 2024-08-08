package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.repository.ShelterRepository;
import pro.sky.telegrambot.util.LocationUtil;

/**
 * Сервис для обработки логики бота.
 */
@Service
public class BotService {

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final Keyboard mainMenuKeyboard;

    public BotService(TelegramBot telegramBot, ShelterRepository shelterRepository) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.mainMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Выбор приюта", "Инструкция как взять животное из приюта"},
                new String[]{"Прислать отчет о питомце", "Позвать волонтера"}
        );
    }

    public void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            long chatId = update.message().chat().id();

            switch (text) {
                case "Выбор приюта":
                    sendShelterChoiceMenu(chatId);
                    break;
                case "Инструкция как взять животное из приюта":
                    sendInstruction(chatId);
                    break;
                case "Прислать отчет о питомце":
                    sendPetReport(chatId);
                    break;
                case "Позвать волонтера":
                    callVolunteer(chatId);
                    break;
                case "Информация о приюте для кошек":
                    sendCatShelterInfo(chatId);
                    break;
                case "Местоположение приюта для кошек":
                    LocationUtil.sendCatShelterLocation(chatId, AnimalType.CAT);
                    break;
                case "Информация о приюте для собак":
                    sendDogShelterInfo(chatId);
                    break;
                case "Местоположение приюта для собак":
                    LocationUtil.sendDogShelterLocation(chatId, AnimalType.DOG);
                    break;
                default:
                    sendMainMenu(chatId);
            }
        }
    }

    private void sendMainMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите опцию:")
                .replyMarkup(mainMenuKeyboard));
    }

    private void sendShelterChoiceMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите приют:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                        new String[]{"Главное меню"}
                )));
    }

    private void sendInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное из приюта: ..."));
    }

    private void sendPetReport(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Прислать отчет о питомце: ..."));
    }

    private void callVolunteer(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Позвать волонтера: ..."));
    }

    private void sendCatShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для кошек: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для кошек", "Главное меню"}
                )));
    }

    private void sendDogShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для собак: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для собак", "Главное меню"}
                )));
    }
}