package pro.sky.telegrambot.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.model.MenuBot;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.PetService;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.File;


@Component
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {
    private Map<Long, String> userStates = new HashMap<>(); //
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+7-9\\d{2}-\\d{3}-\\d{2}");

    @Autowired
    private PetService petService;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBotConfiguration configuration;
//    private final Pet pet;

    private final NotificationTaskRepository repository;
    private final MenuBot menuBot;


    public TelegramBotUpdatesListener(TelegramBotConfiguration configuration, NotificationTaskRepository repository, MenuBot menuBot) {
            this.configuration = configuration;
            this.repository = repository;
            this.menuBot = menuBot;
        }
    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(this);
    }


        @Override
        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                switch (text) {
                    case "/start":
                        startCommandReceived(chatId, text);
                        break;

                    case "/menu":
                        menu(chatId, text);
                        break;

                    case "Информация о приюте":
                        informationAboutShelter(chatId, text);
                        break;

                    case "Расписание и адрес приюта":
                        adressOfShelter(chatId, text);
                        break;

                    case "Оформление пропуска и схема проезда":
                        registrationOfPass(chatId, text);
                        break;

                    case "Техника безопасности":
                        safetyEquipment(chatId, text);
                        break;

//                case "Запросить связь":
//                    contactPhoneNumber(chatId, text);
//                    break;

                    case "Как взять животное из приюта":
                        takeAnimalFromShelter(chatId, text);
                        break;

                    case "Позвать волонтера":
                        callToVolunteer(chatId, text);
                        break;
                    case "Список животных":
                        updateFile("Список животных", "Кот барсик 4 месяца, 3 кг, цвет рыжий\n" +
                                "Кот васька 3 месяца, 2.4 кг, цвет белый");
                        try {
                            sendDocument(310232057L, new File("Список животных"));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        writeIncorrectText(chatId, text);
                        break;

                }
/*
            if (!userStates.containsKey(chatId)) {
                // Новый пользователь
                handleNewRequest(chatId, text);
 */
           /* } else {
                // Уже был контакт с ботом
                handleExistingRequest(chatId, update.getMessage().getText());
            */
            }


        }


        private void startCommandReceived(long chatId, String text) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Привет! Я бот, который поможет вам взаимодействовать с приютом,где бездомные животные находят заботу, уход, безопасность и надежду на новый дом." +
                    "\n" + "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое." +
                    "\n" + "Жми скорее /menu");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException("ошибка");
            }
        }

        private void sendMessage(long chatId, String text) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException();
            }
        }

        private void menu(long chatId, String text) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("выберите услугу");
            message.setReplyMarkup(menuBot.sendMainMenu());
            try {

                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void informationAboutShelter(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.");
                        message.setReplyMarkup(menuBot.sendSubmenu1());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void adressOfShelter(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Наш приют расположен по адресу: г. Красноярск, Советский проспет, д.16.\n" +
                                "Расписание работы приюта:\n" +
                                " - [Понедельник - Пятница: 9:00 - 18:00].\n" +
                                " - [Суббота - Воскресенье: 10:00 - 17:00].\n" +
                                "Чтобы попасть на территорию приюта, необходимо получить пропуск у охраны по предварительной записи.\n" +
                                "Контактные данные охраны: +7-921-911-19-19.");
                        message.setReplyMarkup(menuBot.sendSubmenu1());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void registrationOfPass(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Для оформления пропуска необходимо при себе иметь паспорт.\n" +
                                "После оформления пропуска Вам необходимо пройти в здание 16Д: Схема проезда указана на фото");
                        message.setReplyMarkup(menuBot.sendSubmenu1());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                        sendPhoto(chatId, "asd", "D:\\aa\\telegram-bot2/cat.jpg");
                    }

                    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
                        File imageFile = new File("D:\\aa\\telegram-bot2/cat.jpg");
                        InputFile photo = new InputFile(imageFile);
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(chatId);
                        sendPhoto.setPhoto(photo);
                        try {
                            execute(sendPhoto);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void safetyEquipment(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Вот некоторые правила техники безопасности в приюте для животных:\n" +
                                "1. Проявляйте терпение и уважение к сотрудникам, волонтерам и другим посетителям.\n" +
                                "2. Любые действия в приюте совершаются с разрешения работников или руководства.\n" +
                                "3. На территории приюта не кричите, не размахивайте руками, не бегайте между будками или вольерами, не пугайте и не дразните животных.\n" +
                                "4. Запрещается посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
                                "5. Запрещается самостоятельно открывать вольеры и выводить животное без разрешения сотрудника приюта.\n" +
                                "6. Запрещается подходить близко к вольерам и гладить собак через сетку на выгулах.\n" +
                                "7. Запрещается допускать близкий контакт между собаками во время выгула во избежание драк.\n" +
                                "8. Запрещается отпускать животных с поводка.\n" +
                                "9. Разрешается гулять только на отведенной территории, о которой сообщит работник приюта.\n" +
                                "При несоблюдении правил сотрудники приюта оставляют за собой право отказать посетителю в посещении приюта.");
                        message.setReplyMarkup(menuBot.sendSubmenu1());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

//    private void contactPhoneNumber (long chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Я могу записать Ваши контактные данные и в ближайшее время с Вами свяжется наш волонтер и проконсультируют Вас. " +
//                "Введите номер телефона.");
//        var matcher = PHONE_PATTERN.matcher(text);
//        if (matcher.matches()) {
//            repository.save(text);
//        }
//        //handleContactInput(chatId, text);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException("Неверный формат номера телефона. Пожалуйста, введите номер в формате +7-9---");
//        }
//    }

//    if (matcher.matches()) {
//        var date = parseDate(matcher.group(1));
//        if (date == null) {
//            telegramBot.execute(new SendMessage(chatId, "Неправильный формат даты"));
//            return;
//        }
//        var task = new NotificationTask();
//        task.setText_msg(matcher.group(3));
//        task.setChat_id(chatId);
//        task.setDate(date);
//        repository.save(task);
//        logger.info("Task has been saved: {}", task);
//    }

//    private void contactPhoneNumber(long chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Я могу записать Ваши контактные данные и в ближайшее время с Вами свяжется наш волонтер и проконсультируют Вас. " +
//                "Введите команду /contact, чтобы ввести номер телефона.");
//        handleContactInput(chatId, text);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException("ошибка");
//        }
//    }
//
//    private void handleContactInput(Long chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        Matcher matcher = PHONE_PATTERN.matcher(text);
//        if (matcher.matches()) {
//            // Валидный номер телефона
//            //saveContact(chatId, text);
//            message.setText("Номер телефона успешно сохранен!");
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                throw new RuntimeException("Неверный формат номера телефона. Пожалуйста, введите номер в формате +7-9---");
//            }
//        } else {
//            // Невалидный номер телефона
//            message.setText("Неверный формат номера телефона. Пожалуйста, введите номер в формате +7-9---");
//        }
//    }


                    private void takeAnimalFromShelter(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("В данном разделе я помогу тебе с выбором твоего будущего друга, " +
                                "дам список необходимых документов, чтобы забрать питомца из приюта, " +
                                "дам список рекомендаций по транспортиовке и обустройству дома для питомца " +
                                "и предоствлю контактные данные кинологов для получения советов по общению с питомцем");
                        message.setReplyMarkup(menuBot.sendSubmenu2());
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void callToVolunteer(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Запрос отправлен волонтеру.");
                        sendToVolunteer(String.valueOf(chatId), text);

                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void sendToVolunteer(String chatId, String text) {
                        final String ADMIN_ID = String.valueOf(934262992);
                        try {
                            execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

                    private void writeIncorrectText(long chatId, String text) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Не понял. Давайте попробуем снова. \" +\n" +
                                "Что бы вы хотели сделать? Выберете пункт из /menu");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException("ошибка");
                        }
                    }

/*
    private void handleNewRequest(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        if (text.equalsIgnoreCase("/start")) {
            // Первое приветствие
            execute(new SendMessage(chatId, "Привет! \uD83D\uDC4B Я бот, который поможет вам взаимодействовать с приютом," +
                    "где бездомные животные находят заботу, уход, безопасность и надежду на новый дом. \n" +
                    "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое. \n" +
                    "Жми скорее /menu"));
            message.setReplyMarkup(menuBot.sendMainMenu());
        } else {
            message.setText("Не понял. Давайте попробуем снова. " +
                    "Что бы вы хотели сделать?");
            message.setReplyMarkup(menuBot.sendMainMenu());
        }
    }
 */
/*
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        try {
            logger.info("Processing update: {}", update);
            var message = update.getMessage();
            if (message != null) {
                var text = update.getMessage().getText();
                var chatId = update.getMessage().getChatId().toString();
                if (text != null) {
                    SendMessage sendMessage = null;
                    if ("/start".equals(text)) {
                        // Используем execute из TelegramLongPollingBot
                        execute(new SendMessage(chatId, "Привет! \uD83D\uDC4B Я бот, который поможет вам взаимодействовать с приютом," +
                                "где бездомные животные находят заботу, уход, безопасность и надежду на новый дом. \n" +
                                "Я могу рассказать вам о приюте, о его питомцах, как помочь питомцу найти свой дом, какие документы для этого необходимы и многое другое. \n" +
                                "Жми скорее /menu"));
                    } else if ("/menu".equals(text)) {
                        sendMessage = new SendMessage(chatId, "выберите услугу");
                        // Добавляем ReplyMarkup к SendMessage
                        sendMessage.setReplyMarkup(menuBot.sendMainMenu());
                        // Используем execute из TelegramLongPollingBot
                        execute(sendMessage);
                    } else if ("Информация о приюте".equals(text)) {
                        // "Кнопка 1"
                        // Создаем новый объект SendMessage здесь:
                        sendMessage = new SendMessage(chatId, "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);
                    } else if ("Расписание и адрес приюта".equals(text)) {
                        sendMessage = new SendMessage(chatId, "Наш приют расположен по адресу: г. Красноярск, Советский проспет, д.16.\n" +
                                "Расписание работы приюта:\n" +
                                " - [Понедельник - Пятница: 9:00 - 18:00].\n" +
                                " - [Суббота - Воскресенье: 10:00 - 17:00].\n" +
                                "Чтобы попасть на территорию приюта, необходимо получить пропуск у охраны по предварительной записи.\n" +
                                "Контактные данные охраны: +7-921-911-19-19.");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);
                    } else if ("Оформление пропуска и схема проезда".equals(text)) {
                        sendMessage = new SendMessage(chatId, "Для оформления пропуска необходимо при себе иметь паспорт.\n" +
                                "После оформления пропуска Вам необходимо пройти в здание 16Д: Схема проезда указана на фото");
sendPhoto(chatId, "asd", "D:\aa\telegram-bot2/cat.jpg");
                        //sendPhoto(chatId, "asd", "static/123.jpg");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu1());
                        execute(sendMessage);
//telegramBotAnimal/src/main/resources/static/123.jpg
                } else if ("Как взять животное из приюта".equals(text)) {

                        //  "Кнопка 2"
                        sendMessage = new SendMessage(chatId, "Как взять животное из приюта");
                        sendMessage.setReplyMarkup(menuBot.sendSubmenu2());
                        execute(sendMessage);
                    } else if ("Прислать отчет о питомце".equals(text)) {
                        // "Кнопка 3"
                        execute(new SendMessage(chatId, "Питомец чувствует себя хорошо"));
                    } else if ("Позвать волонтера".equals(text)) {
                        // "Кнопка 4"
                        sendMessage = new SendMessage(chatId, "Запрос отправлен волонтеру.");
                        execute(sendMessage);
                        sendToVolunteer(chatId, text);                    }
                }
            }
        } catch (TelegramApiException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private void handleNewRequest(long chatId, String text) {
    }
    public void sendPhoto(String chatId, String imageCaption, String imagePath) throws FileNotFoundException, TelegramApiException {
        File imageFile = new File("D:\aa\telegram-bot2/cat.jpg");
        InputFile photo = new InputFile(imageFile);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photo);
        execute(sendPhoto);
    }
//    public void sendPhoto(String chatId, String imageCaption, String imagePath) throws FileNotFoundException, TelegramApiException {
//        File image = ResourceUtils.getFile("classpath:" + imagePath);
//        //File image = new File("D:\aa\telegram-bot2/cat.jpg");
//        SendPhoto sendPhoto = new SendPhoto();
//        System.out.println(image.getAbsolutePath());
//        sendPhoto.setChatId(chatId);
//        // Устанавливаем путь к файлу с изображением
//        sendPhoto.setCaption(imageCaption);
//        execute(sendPhoto);
//    }
    private void sendToVolunteer(String chatId, String text) throws TelegramApiException {
        final String ADMIN_ID = String.valueOf(934262991);
        execute(new SendMessage(ADMIN_ID, "Новое обращение от @" + chatId + ": " + text));
    }
 */
                    public void sendDocument(long chatId, File file) throws TelegramApiException {
                        SendDocument request = new SendDocument();
                        request.setChatId(chatId);
                        request.setDocument(new InputFile(file));
                        execute(request);


                    }
                    public void updateFile(String file, String content) {
                        try (var out = new BufferedWriter(new FileWriter(file))) {
                            out.write(content);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    @Override
                    public String getBotToken() {
                        return configuration.getToken();
                    }

                    @Override
                    public String getBotUsername() {
                        return configuration.getName();
                    }
                }




