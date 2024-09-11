package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.command.*;
import pro.sky.telegrambot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sky.telegrambot.repository.*;
import java.util.*;

import static pro.sky.telegrambot.model.BotCommand.*;


/**
 * Сервис для обработки логики бота.
 */
@Service
public class BotService {

    private final ChatService chatService;
    private final ClientService clientService;
    private final VolunteerService volunteerService;
    private final FileService fileService;
    private final Keyboard mainMenuKeyboard;
    private final Keyboard instructionMenuKeyboard;
    private final Keyboard petReportMenuKeyboard;
    private final Keyboard volunteerMenuKeyboard;
    private final Keyboard chatMenuKeyboard;
    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final Map<Long, Integer> incorrectCounts = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);
    private final UserRepository userRepository;
    private PetService petService;
    private final PetRepository petRepository;
    private final ReportRepository reportRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Map<Long, UserState> userStates = new HashMap<>();
    private final Map<Long, PetRegistrationState> petRegistrationMap = new HashMap<>();
    private final Map<Long, PetReportState> checkPetReportMap = new HashMap<>();





    @Autowired
    public BotService(TelegramBot telegramBot, ShelterRepository shelterRepository, ChatService chatService,
                      ClientService clientService, VolunteerService volunteerService,
                      FileService fileService, ReportRepository reportRepository, UserRepository userRepository, PetRepository petRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.petRepository = petRepository;
        this.chatService = chatService;
        this.clientService = clientService;
        this.volunteerService = volunteerService;
        this.fileService = fileService;
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.clientRepository = clientRepository;
        this.mainMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Выбор приюта", "Инструкция как взять животное из приюта"},
                new String[]{"Прислать отчет о питомце"},
                new String[]{"Позвать волонтера"}
        );
        this.instructionMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Инструкция по знакомству с животным", "Правила безопасности на территории приюта"},
                new String[]{"Инструкция по обустройству дома", "Рекомендации по проверенным кинологам", "Причины для отказа"},
                new String[]{"Обустройство дома для питомца с ограниченными возможностями", "Информация по транспортировке питомца"},
                new String[]{"Позвать волонтера", "Главное меню"}
        );
        this.petReportMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Форма ежедневного отчета", "Отправить отчет"},
                new String[]{"Позвать волонтера", "Главное меню"}
        );
        this.volunteerMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Активен", "Неактивен"},
                new String[]{"Добавить питомца", "Посмотреть питомцев", "Посмотреть отчеты"},
                new String[]{"Главное меню"}
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



            }
        }
    }

    public void safetyEquipment(long chatId) {
        String safetyRules = "**Правила безопасности на территории приюта:**\n" +
                "* Проявляйте уважение к животным и сотрудникам приюта.\n" +
                "* Не кормите животных без разрешения.\n" +
                "* Не гладьте животных через решетку.\n" +
                "* Соблюдайте тишину и порядок.\n" +
                "* Следуйте указаниям сотрудников.\n" +
                "* Запрещается мусорить и оставлять после себя отходы.\n" +
                "* Запрещается приводить с собой других животных без согласования с администрацией.\n" +
                "* Запрещается курить на территории приюта.\n" +
                "* Дети до 14 лет должны находиться под присмотром взрослых.";
        SendMessage message = new SendMessage(chatId, safetyRules);
        telegramBot.execute(message);
    }


    public void sendDailyReportForm(long chatId) {
        String dailyReport = "**В ежедневный отчет входит следующая информация:**\n" +
                "\n" +
                "- *Фото животного.*\n" +
                "(Фотография и текст должны быть одним сообщением)" +
                "- *Рацион животного.*\n" +
                "- *Общее самочувствие и привыкание к новому месту.*\n" +
                "- *Изменения в поведении: отказ от старых привычек, приобретение новых.*\n" +
                "\n" +
                "Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет. Каждый день после 21:00 волонтеры отсматривают все присланные отчеты," +
                " и в случае некорректного заполнения тебе в телеграмм поступит напоминание от бота о правильности заполнения отчета.\n" +
                "\n" +
                "Если ты не будешь присылать ежедневный отчет, то по истечении 2 дней волонтеры будут обязаны самолично проверять условия содержания животного. \n" +
                "Как только период в 30 дней заканчивается, волонтеры принимают решение о том, остается животное у хозяина или нет. Испытательный срок может быть пройден, может быть продлен на срок еще 14 или 30 дней, а может быть не пройден.";
        SendMessage message = new SendMessage(chatId, dailyReport);
        telegramBot.execute(message);
    }




    private void getAdviceFromDogHandler(long chatId) {
        String text = "Cоветы кинолога по первичному общению с собакой можно получить по этой ссылке: \n" +
                "https://yunavet.ru/useful/sovety-kinologa-chto-neobhodimo-znat-hozjainu-o-svoej-sobake/";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }



    public void getReasonsForRefusal(long chatId) {
        String text = "Список причин, почему могут отказать и не дать забрать собаку из приюта: \n" +
                "1. Отказ обеспечить безопасность питомца на новом месте. \n" +
                "2. Нестабильные отношения в семье. \n" +
                "3. Антинаучное мышление. \n" +
                "4. Наличие дома большого количества животных.\n" +
                "5. Маленькие дети в семье.\n" +
                "6. Аллергия.\n" +
                "7. Животное забирают в подарок кому-то. \n" +
                "8. Животное забирают в целях использования его рабочих качеств.\n" +
                "9. Отказ приехать познакомиться с животным.\n" +
                "10. Претендент — пожилой человек, проживающий один.\n" +
                "11. Отсутствие регистрации и собственного жилья или его несоответствие нормам приюта.\n" +
                "12. Без объяснения причин.\n" +
                "Такое тоже бывает, потому что не всегда удобно сказать человеку о своих подозрениях и сомнениях. \n" +
                "Простой пример: к будущим хозяевам черных кошек, особенно перед Хеллоуином, присматриваются особенно пристально.";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }


    public void sendPetReport(long chatId) {
        checkPetReportMap.put(chatId, new PetReportState());
        telegramBot.execute(new SendMessage(chatId, "Введите кличку питомца:"));
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
     * Отправляет рекомендации по проверенным кинологам.
     *
     * @param chatId идентификатор чата
     */
    public void sendCynologistRecommendations(long chatId) {
        telegramBot.execute(new SendMessage(chatId,  "Мной дан список проверенных кинологов для общения с ними:\n" +
                "1. Алексей, 43 года. Стаж: 20 лет. Контактные данные:+7-923-232-34-54. \n" +
                "2. Георгий, 30 лет. Стаж: 7 лет. Контактные данные:+7-923-555-30-90. \n" +
                "3. Юлия, 26 лет. Стаж: 3 года. Контактные данные:+7-923-987-78-79. \n"));
    }


    public void sendInstructionMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите инструкцию:")
                .replyMarkup(instructionMenuKeyboard));

    }

    public void sendReportMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Мы очень рады, что у нашего питомца появился новый друг! Но тем не менее мы переживаем за наших питомцев.\" +\n" +
                "                \"Мы хотели бы чтобы ты оповещал нас о состоянии питомца на протяжении 30 дней.\" +\n" +
                "                \"В данном разделе ты можешь получить образец формы ежедневного отчета, на основании, которого ты можешь нас оповещать о состоянии питомца")
                .replyMarkup(petReportMenuKeyboard));
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
       //sendVolunteerMenu(chatId);
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

    public void informationAboutShelter(long chatId) {
        SendMessage message = new SendMessage(chatId, "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.").replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                new String[]{"Главное меню"}
        ));
        telegramBot.execute(message);
    }

    public void getRecommendationsHomeImprovementForDisabledPet(long chatId) {
        String text = "Рекомендации по обустройству дома для питомца с ограниченными возможностями Вы получите дополнительно по этой ссылке: \n" +
                "https://petscage.ru/blog/kak-oblegchit-zhizn-zhivotnomu-invalidu/?srsltid=AfmBOorBT8vqYA28euOHitmhcZBTN-B3c8JjIqCxpK_5K7XBOM_UBjkC";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    public void getRecommendationsAnimalTransportation(long chatId) {
        String text = "Рекомендации по транспортировке питомца Вы получите по этой ссылке: \n" +
                "(https://www.purina.ru/find-a-pet/articles/getting-a-dog/adoption/adopting-a-dog)*.";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }


    public void sendMainMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите опцию:")
                .replyMarkup(mainMenuKeyboard));
    }


    public void sendShelterChoiceMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите приют:")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                        new String[]{"Выбрать питомца"},
                        new String[]{"Главное меню"}
                )));
    }


    private void sendInstruction(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное из приюта: ...").replyMarkup(instructionMenuKeyboard));

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

    }







