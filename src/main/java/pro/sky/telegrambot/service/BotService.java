package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.BotCommand;
import pro.sky.telegrambot.repository.ShelterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сервис для обработки логики бота.
 */
@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final ChatService chatService;
    private final FileService fileService;
    private final VolunteerService volunteerService;
    private final ClientService clientService;
    private final Keyboard mainMenuKeyboard;
    private final Keyboard instructionMenuKeyboard;
    private final Keyboard volunteerMenuKeyboard;
    private final Keyboard chatMenuKeyboard;

    @Autowired
    public BotService(TelegramBot telegramBot, ShelterRepository shelterRepository,
                      ChatService chatService, FileService fileService, VolunteerService volunteerService,
                      ClientService clientService) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.chatService = chatService;
        this.fileService = fileService;
        this.volunteerService = volunteerService;
        this.clientService = clientService;
        this.mainMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Выбор приюта", "Инструкция как взять животное из приюта"},
                new String[]{"Прислать отчет о питомце", "Позвать волонтера"}
        );
        this.instructionMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Инструкция по знакомству с животным", "Инструкция как взять"},
                new String[]{"Инструкция по обустройству дома", "Рекомендации по проверенным кинологам"},
                new String[]{"Позвать волонтера", "Главное меню"}
        );
        this.volunteerMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Активен", "Неактивен"},
                new String[]{"Главное меню"}
        );
        this.chatMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Прекратить беседу с клиентом"}
        );
    }

    /**
     * Обрабатывает обновление из Telegram.
     * @param update обновление из Telegram
     */
    public void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            long chatId = update.message().chat().id();

            if (chatService.isActiveChat(chatId)) {
                chatService.routeMessage(chatId, update.message());
            } else {
                BotCommand command = BotCommand.fromString(text);
                command.execute(this, chatId);
            }
        }
    }

    // Методы для выполнения команд
    public void sendMainMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите опцию:").replyMarkup(mainMenuKeyboard));
    }

    public void sendShelterChoiceMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите приют:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                        new String[]{"Главное меню"}
                )));
    }

    public void sendInstructionMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите инструкцию:")
                .replyMarkup(instructionMenuKeyboard));
    }

    public void sendPetReport(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Отправьте отчет о питомце: ..."));
    }

    public void sendCatShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для кошек: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для кошек", "Главное меню"}
                )));
    }

    public void sendDogShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для собак: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для собак", "Главное меню"}
                )));
    }

    public void sendMeetAnimalInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция по знакомству с животным:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    public void sendTakeAnimalInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    public void sendSetupHomeInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция по обустройству дома:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    public void sendCynologistRecommendations(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Рекомендации по проверенным кинологам: ..."));
    }

    public void sendFile(long chatId, String filePath) {
        fileService.sendFile(chatId, filePath);
    }

    public void callVolunteer(long chatId) {
        clientService.callVolunteer(chatId);
    }

    public void joinChat(long chatId) {
        long clientChatId = chatService.getClientChatIdForVolunteer(chatId);
        if (clientChatId != 0) {
            logger.info("Volunteer {} joining chat with client {}", chatId, clientChatId);
            telegramBot.execute(new SendMessage(clientChatId, "Волонтер присоединился к беседе."));
            telegramBot.execute(new SendMessage(chatId, "Вы присоединились к беседе с клиентом.").replyMarkup(chatMenuKeyboard));
            chatService.startChat(clientChatId, chatId);
        } else {
            logger.warn("No client found for volunteer {}", chatId);
            telegramBot.execute(new SendMessage(chatId, "Не удалось найти клиента для подключения."));
        }
    }

    public void leaveChat(long chatId) {
        chatService.endChat(chatId);
    }

    public void registerVolunteer(long chatId) {
        volunteerService.registerVolunteer(chatId);
        sendVolunteerMenu(chatId);
    }

    public void setVolunteerActive(long chatId, boolean active) {
        volunteerService.setVolunteerActive(chatId, active);
    }

    public void sendVolunteerMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите статус:").replyMarkup(volunteerMenuKeyboard));
    }

    public void endChat(long chatId) {
        long clientChatId = chatService.getClientChatIdForVolunteer(chatId);
        chatService.endChat(chatId);
        if (clientChatId != 0) {
            telegramBot.execute(new SendMessage(clientChatId, "Волонтер прекратил беседу с вами."));
        }
        telegramBot.execute(new SendMessage(chatId, "Вы прекратили беседу с клиентом."));
    }
}