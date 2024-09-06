package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.BotCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.telegrambot.model.PetRegistrationState;
import pro.sky.telegrambot.model.PetRegistrationStep;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для обработки логики бота.
 */
@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    private final TelegramBot telegramBot;
    private final ChatService chatService;
    private final ClientService clientService;
    private final VolunteerService volunteerService;
    private final FileService fileService;
    private final Keyboard mainMenuKeyboard;
    private final Keyboard instructionMenuKeyboard;
    private final Keyboard volunteerMenuKeyboard;
    private final Keyboard chatMenuKeyboard;
    private final Map<Long, PetRegistrationState> petRegistrationMap = new HashMap<>();

    @Autowired
    public BotService(TelegramBot telegramBot, ChatService chatService,
                      ClientService clientService, VolunteerService volunteerService,
                      FileService fileService) {
        this.telegramBot = telegramBot;
        this.chatService = chatService;
        this.clientService = clientService;
        this.volunteerService = volunteerService;
        this.fileService = fileService;
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
                new String[]{"Добавить питомца", "Посмотреть питомцев"}
//                new String[]{"Главное меню"}
        );
        this.chatMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Завершить беседу с клиентом"}
        );
    }

    /**
     * Обрабатывает обновление из Telegram.
     *
     * @param update обновление из Telegram
     */
    public void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            long chatId = update.message().chat().id();

            BotCommand command = BotCommand.fromString(text);
            if (command != null) {
                command.execute(this, chatId);
            } else if (chatService.isActiveChat(chatId)) {
                chatService.routeMessage(chatId, update.message());
            } else {
                //BotCommand.DEFAULT.execute(this, chatId);
            }
        }
//        long chatId = update.message().chat().id();
//        String text = update.message().text();
//        PetRegistrationState state = petRegistrationMap.get(chatId);
//
//        if (state != null) {
//            if (state.getStep() == PetRegistrationStep.NAME) {
//                state.setName(text);
//                state.nextStep();
//                telegramBot.execute(new SendMessage(chatId, "Введите породу питомца:"));
//            } else if (state.getStep() == PetRegistrationStep.BREED) {
//                state.setBreed(text);
//                state.nextStep();
//                telegramBot.execute(new SendMessage(chatId, "Загрузите фотографию питомца:"));
//            } else if (state.getStep() == PetRegistrationStep.PHOTO && update.message().photo() != null) {
//                String filePath = savePhoto(update.message().photo());
//                state.setPhotoPath(filePath);
//                int petId = volunteerService.addPet(state.getName(), state.getBreed(), state.getPhotoPath());
//                telegramBot.execute(new SendMessage(chatId, "Питомец зарегистрирован под номером " + petId));
//                petRegistrationMap.remove(chatId);
//            } else {
//                telegramBot.execute(new SendMessage(chatId, "Ожидается фотография. Попробуйте снова."));
//            }
//        } else {
//            BotCommand command = BotCommand.fromString(text);
//            if (command != null) {
//                command.execute(this, chatId);
//            } else {
//                BotCommand.DEFAULT.execute(this, chatId);
//            }
//        }

    }

    // Методы для выполнения команд

    /**
     * Отправляет главное меню.
     *
     * @param chatId идентификатор чата
     */
    public void sendMainMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите опцию:").replyMarkup(mainMenuKeyboard));
    }

    /**
     * Отправляет меню выбора приюта.
     *
     * @param chatId идентификатор чата
     */
    public void sendShelterChoiceMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите приют:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                        new String[]{"Выбрать питомца"},
                        new String[]{"Главное меню"}
                )));
    }

    /**
     * Отправляет меню инструкций.
     *
     * @param chatId идентификатор чата
     */
    public void sendInstructionMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите инструкцию:")
                .replyMarkup(instructionMenuKeyboard));
    }

    /**
     * Отправляет отчет о питомце.
     *
     * @param chatId идентификатор чата
     */
    public void sendPetReport(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Отправьте отчет о питомце: ..."));
    }

    /**
     * Отправляет информацию о приюте для кошек.
     *
     * @param chatId идентификатор чата
     */
    public void sendCatShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для кошек: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для кошек", "Главное меню"}
                )));
    }

    /**
     * Отправляет информацию о приюте для собак.
     *
     * @param chatId идентификатор чата
     */
    public void sendDogShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Информация о приюте для собак: ...")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Местоположение приюта для собак", "Главное меню"}
                )));
    }

    /**
     * Отправляет инструкцию по знакомству с животным.
     *
     * @param chatId идентификатор чата
     */
    public void sendMeetAnimalInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция по знакомству с животным:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    /**
     * Отправляет инструкцию как взять животное.
     *
     * @param chatId идентификатор чата
     */
    public void sendTakeAnimalInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    /**
     * Отправляет инструкцию по обустройству дома.
     *
     * @param chatId идентификатор чата
     */
    public void sendSetupHomeInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция по обустройству дома:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Получить файл", "Назад"}
                )));
    }

    /**
     * Отправляет рекомендации по проверенным кинологам.
     *
     * @param chatId идентификатор чата
     */
    public void sendCynologistRecommendations(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Рекомендации по проверенным кинологам: ..."));
    }

    /**
     * Отправляет файл в чат.
     *
     * @param chatId   идентификатор чата
     * @param filePath путь к файлу
     */
    public void sendFile(long chatId, String filePath) {
        fileService.sendFile(chatId, filePath);
    }

    /**
     * Вызывает волонтера для чата.
     *
     * @param chatId идентификатор чата
     */
    public void callVolunteer(long chatId) {
        clientService.callVolunteer(chatId);
    }

    /**
     * Присоединяется к беседе с клиентом.
     *
     * @param chatId идентификатор чата
     */
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

    /**
     * Попрощаться с клиентом и завершить беседу.
     *
     * @param chatId идентификатор чата
     */
    public void leaveChat(long chatId) {
        chatService.endChat(chatId);
    }

    /**
     * Регистрирует волонтера и отправляет меню волонтера.
     *
     * @param chatId идентификатор чата
     */
    public void registerVolunteer(long chatId) {
        volunteerService.registerVolunteer(chatId);
//        sendVolunteerMenu(chatId);
    }

    /**
     * Устанавливает волонтера активным.
     *
     * @param chatId идентификатор чата
     * @param active статус активности
     */
    public void setVolunteerActive(long chatId, boolean active) {
        volunteerService.setVolunteerActive(chatId, active);
    }

    /**
     * Отправляет меню волонтера.
     *
     * @param chatId идентификатор чата
     */
    public void sendVolunteerMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите статус:").replyMarkup(volunteerMenuKeyboard));
    }

    /**
     * Завершает беседу с клиентом.
     *
     * @param chatId идентификатор чата
     */
    public void endChat(long chatId) {
        long clientChatId = chatService.getClientChatIdForVolunteer(chatId);
        chatService.endChat(chatId);
        if (clientChatId != 0) {
            telegramBot.execute(new SendMessage(clientChatId, "Волонтер прекратил беседу с вами."));
        }
        telegramBot.execute(new SendMessage(chatId, "Вы прекратили беседу с клиентом."));
        sendMainMenu(clientChatId);  // Возврат к исходному состоянию
        sendVolunteerMenu(chatId);   // Возврат к исходному состоянию
    }

    public void startPetRegistration(long chatId) {
        petRegistrationMap.put(chatId, new PetRegistrationState());
        telegramBot.execute(new SendMessage(chatId, "Введите кличку питомца:"));
    }

    private String savePhoto(PhotoSize[] photo) {
        // Логика сохранения фотографии и возвращение пути к файлу
        return "/src/to/saved/photo.jpg";
    }

    public VolunteerService getVolunteerService() {
        return volunteerService;
    }
}
