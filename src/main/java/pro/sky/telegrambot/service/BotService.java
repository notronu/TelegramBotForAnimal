package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.request.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.model.PetReport;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.repository.ShelterRepository;
import pro.sky.telegrambot.repository.UserRepository;
import pro.sky.telegrambot.util.LocationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.text;


/**
 * Сервис для обработки логики бота.
 */
@Service
public class BotService {

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final Keyboard mainMenuKeyboard;
    private final Keyboard instructionMenuKeyboard;
    private final Keyboard petReportMenuKeyboard;
    private final Map<Long, Integer> incorrectCounts = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(BotService.class);
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+7-9\\d{2}-\\d{3}-\\d{2}-\\d{2}");
    private static final Pattern ANSWER_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private static final Pattern ANSWER_PATTERN1 = Pattern.compile("([\\W+]+)(\n)([\\W+]+)(\n)([\\W+]+)");
    private static final Pattern ANSWER_PATTERN2 = Pattern.compile("[0-9]");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private UserService userService;
    private final UserRepository repository;
    private PetService petService;
    private final ReportRepository reportRepository;
    private final PetRepository petRepository;

    private Map<Long, String> userStates = new HashMap<>();


    public BotService(TelegramBot telegramBot, ShelterRepository shelterRepository, UserRepository repository, UserService userService, ReportRepository reportRepository, PetRepository petRepository) {
        this.userService = userService;
        this.repository = repository;
        this.reportRepository = reportRepository;
        this.petRepository = petRepository;
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.mainMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Выбор приюта", "Инструкция как взять животное из приюта"},
                new String[]{"Прислать отчет о питомце", "Позвать волонтера"},
                new String[]{"Информация о приюте "}
        );
        this.instructionMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Инструкция по знакомству с животным", "Правила безопасности на территории приюта"},
                new String[]{"Инструкция по обустройству дома", "Рекомендации по проверенным кинологам"},
                new String[]{"Обустройство дома для питомца с ограниченными возможностями", "Информация по транспортировке питомца"},
                new String[]{"Позвать волонтера", "Запросить связь", "Главное меню"}
        );
        this.petReportMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Форма ежедневного отчета", "Отправить отчет"},
                new String[]{"Позвать волонтера", "Главное меню"});

    }

    public void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            long chatId = update.message().chat().id();
            String login = update.message().from().lastName();
            var state = userStates.get(chatId);
            var user = userService.findByUser(chatId);
            boolean understood = false;

            if ("PhoneListener".equals(state)) {
                handleContactInput(chatId, text);
                userStates.remove(chatId);
            }
                 else if ("SendReport".equals(state)) {
                    addReportInRepository(chatId, text);
                    userStates.remove(chatId);
            } else {
                switch (text) {
                    case "Выбор приюта":
                        sendShelterChoiceMenu(chatId);
                        understood = true;
                        break;
                    case "Информация о приюте":
                        informationAboutShelter(chatId);
                        understood = true;
                        break;
                    case "Инструкция как взять животное из приюта":
                        sendInstruction(chatId);
                        understood = true;
                        break;
                    case "Позвать волонтера":
                        callVolunteer(chatId);
                        understood = true;
                        break;
                    case "Информация о приюте для кошек":
                        sendCatShelterInfo(chatId);
                        understood = true;
                        break;
                    case "Местоположение приюта для кошек":
                        LocationUtil.sendCatShelterLocation(chatId, AnimalType.CAT);
                        understood = true;
                        break;
                    case "Правила безопасности на территории приюта":
                        safetyEquipment(chatId, text);
                        understood = true;
                        break;
                    case "Информация о приюте для собак":
                        sendDogShelterInfo(chatId);
                        understood = true;
                        break;
                    case "Местоположение приюта для собак":
                        LocationUtil.sendDogShelterLocation(chatId, AnimalType.DOG);
                        understood = true;
                        break;
                    case "Информация по транспортировке питомца":
                        getRecommendationsAnimalTransportation(chatId);
                        understood = true;
                        break;
                    case "Обустройство дома для питомца с ограниченными возможностями":
                        getRecommendationsHomeImprovementForDisabledPet(chatId);
                        understood = true;
                        break;
                    case "Запросить связь":
                        writeDownContactPhoneNumber(chatId);
                        understood = true;
                        break;
                    case "Форма ежедневного отчета":
                        sendDailyReportForm(chatId);
                        understood = true;
                        break;
                    case "Отправить отчет":
                        sendReport(chatId);
                        understood = true;
                        break;
                    case "Прислать отчет о питомце":
                        sendPetReport(chatId);
                        understood = true;
                        break;
                    default:
                        sendMainMenu(chatId);
                        break;

                }

                if (!understood) {
                    var count = incorrectCounts.getOrDefault(chatId, 0);
                    if (count < 2) {
                        incorrectCounts.put(chatId, count + 1);
                        writeIncorrectText(chatId);
                    } else {
                        writeIncorrectText2(chatId);
                    }
                }
            }
        }
    }

    private void sendDailyReportForm(long chatId) {
        String text = "В ежедневный отчет входит следующая информация: \n" +
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
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }


    private void sendReport(long chatId) {
        String text = """
                Здесь необходмо составить отчет и отправить нашему сотруднику.\s
                Образец отчета смотри ниже. Есть 3 темы. \s
                Рацион животного.\s
                Общее самочувствие и привыкание к новому месту.\s
                Изменения в поведении: отказ от старых привычек, приобретение новых.\s

                Каждая тема с новой строки.""";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
        userStates.put(chatId, "SendReport");
    }

    private void sendPetReport(long chatId) {
        String text = "Мы очень рады, что у нашего питомца появился новый друг! Но тем не менее мы переживаем за наших питомцев." +
                "Мы хотели бы чтобы ты оповещал нас о состоянии питомца на протяжении 30 дней." +
                "В данном разделе ты можешь получить образец формы ежедневного отчета, на основании, которого ты можешь нас оповещать о состоянии питомца";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    private void addReportInRepository(Long chatId, String text) {
        Matcher matcher = ANSWER_PATTERN1.matcher(text);
        if (matcher.matches()) {
            var task = new PetReport();
            task.setAnimalsDiet(matcher.group(1));
            task.setAnimalHealth(matcher.group(3));
            task.setAnimalHabits(matcher.group(5));
            task.setData(LocalDateTime.now());
            task.setUser(repository.findByChatId(chatId));
            reportRepository.save(task);
            SendMessage message = new SendMessage(chatId, "Отчет успешно добавлен");
            telegramBot.execute(message);
        } else {
            SendMessage message = new SendMessage(chatId, "Неверный формат отчета");
            telegramBot.execute(message);
        }
    }
        private void writeDownContactPhoneNumber(long chatId) {
            String text = "Я могу записать Ваши контактные данные и в ближайшее время с Вами свяжется наш волонтер и проконсультируют Вас. " +
                    "Введите номер телефона";
            SendMessage message = new SendMessage(chatId, text);
            telegramBot.execute(message);
            userStates.put(chatId, "PhoneListener");
        }

        //Метод, определяющий правильность номера телефона и позволяющий записать контактные данные в БД при корректном их написании
        private void handleContactInput(Long chatId, String text) {
            Matcher matcher = PHONE_PATTERN.matcher(text);
            if (matcher.matches()) {
                var task = repository.findByChatId(chatId);
                task.setPhone(text);
                repository.save(task);
                SendMessage message = new SendMessage(chatId, "Номер телефона успешно сохранен! Нажмите кнопку /menu");
                telegramBot.execute(message);
            } else {
                SendMessage message = new SendMessage(chatId, "Неверный формат номера телефона. Пожалуйста, введите номер в формате:" + "+7-9**-**-**");
                telegramBot.execute(message);
            }
        }

    public void sendInstructionMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите инструкцию:")
                .replyMarkup(instructionMenuKeyboard));

    }

    private void writeIncorrectText(long chatId) {
        SendMessage message = new SendMessage(chatId, "Не понял. Давайте попробуем снова.\n" +
                "Что бы вы хотели сделать? Выберете пункт из /menu");
        telegramBot.execute(message);
    }

    private void writeIncorrectText2(long chatId) {
        SendMessage message = new SendMessage(chatId, "Может тогда вызвать волонтера?");
        telegramBot.execute(message);
        callVolunteer(chatId);
    }

    private void informationAboutShelter(long chatId)  {
        SendMessage message = new SendMessage(chatId, "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.").replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                new String[]{"Главное меню"}
        ));
        telegramBot.execute(message);
    }

    private void getRecommendationsHomeImprovementForDisabledPet(long chatId) {
        String text = "Рекомендации по обустройству дома для питомца с ограниченными возможностями Вы получите дополнительно по этой ссылке: \n" +
                "https://petscage.ru/blog/kak-oblegchit-zhizn-zhivotnomu-invalidu/?srsltid=AfmBOorBT8vqYA28euOHitmhcZBTN-B3c8JjIqCxpK_5K7XBOM_UBjkC";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    private void getRecommendationsAnimalTransportation(long chatId) {
        String text = "Рекомендации по транспортировке питомца Вы получите по этой ссылке: \n" +
                "(https://www.purina.ru/find-a-pet/articles/getting-a-dog/adoption/adopting-a-dog)*.";
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    private void safetyEquipment(long chatId, String text) {
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
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное из приюта: ...").replyMarkup(instructionMenuKeyboard));;
    }


    private void callVolunteer(long chatId) {
        try {
            // Отправляем сообщение пользователю
            telegramBot.execute(new SendMessage(chatId, "Запрос отправлен волонтеру."));

            // Отправляем сообщение волонтеру
            sendVolunteer(chatId);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения пользователю", e);
            telegramBot.execute(new SendMessage(chatId, "Произошла ошибка при обработке запроса."));
        }
    }

    private void sendVolunteer(long chatId) throws TelegramApiException {
        final long ADMIN_ID = 76421741;
        telegramBot.execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
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
