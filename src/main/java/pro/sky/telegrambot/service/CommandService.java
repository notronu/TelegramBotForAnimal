package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.util.LocationUtil;

import java.io.File;

/**
 * Сервис для обработки команд и делегирования задач соответствующим сервисам.
 */
@Service
public class CommandService {

    private final TelegramBot telegramBot;
    private final ClientService clientService;
    private final VolunteerService volunteerService;
    private final ChatService chatService;

    @Autowired
    public CommandService(TelegramBot telegramBot, ClientService clientService, VolunteerService volunteerService,
                          ChatService chatService) {
        this.telegramBot = telegramBot;
        this.clientService = clientService;
        this.volunteerService = volunteerService;
        this.chatService = chatService;
    }

    /**
     * Отправляет меню выбора приюта.
     * @param chatId ID чата
     */
    public void sendShelterChoiceMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите приют:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                        new String[]{"Главное меню"}
                )));
    }

    /**
     * Отправляет меню инструкций.
     * @param chatId ID чата
     */
    public void sendInstructionMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите инструкцию:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Инструкция по знакомству с животным", "Инструкция как взять"},
                        new String[]{"Инструкция по обустройству дома", "Рекомендации по проверенным кинологам"},
                        new String[]{"Позвать волонтера", "Главное меню"}
                )));
    }

    /**
     * Отправляет отчет о питомце.
     * @param chatId ID чата
     */
    public void sendPetReport(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Прислать отчет о питомце: ..."));
    }

    /**
     * Отправляет информацию о приюте для кошек.
     * @param chatId ID чата
     */
    public void sendCatShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для кошек: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для кошек", "Главное меню"}
                )));
    }

    /**
     * Отправляет информацию о приюте для собак.
     * @param chatId ID чата
     */
    public void sendDogShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для собак: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для собак", "Главное меню"}
                )));
    }

    /**
     * Отправляет инструкцию по знакомству с животным.
     * @param chatId ID чата
     */
    public void sendMeetAnimalInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция по знакомству с животным:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    /**
     * Отправляет инструкцию как взять животное.
     * @param chatId ID чата
     */
    public void sendTakeAnimalInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    /**
     * Отправляет инструкцию по обустройству дома.
     * @param chatId ID чата
     */
    public void sendSetupHomeInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция по обустройству дома:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    /**
     * Отправляет рекомендации по проверенным кинологам.
     * @param chatId ID чата
     */
    public void sendCynologistRecommendations(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Рекомендации по проверенным кинологам: ..."));
    }

    /**
     * Отправляет файл.
     * @param chatId ID чата
     * @param filePath путь к файлу
     */
    public void sendFile(long chatId, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            telegramBot.execute(new SendDocument(chatId, file));
        } else {
            telegramBot.execute(new SendMessage(chatId, "Извините, файл не найден."));
        }
    }

    /**
     * Отправляет главное меню.
     * @param chatId ID чата
     */
    public void sendMainMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите опцию:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Выбор приюта", "Инструкция как взять животное из приюта"},
                        new String[]{"Прислать отчет о питомце", "Позвать волонтера"}
                )));
    }
}