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
import pro.sky.telegrambot.repository.ShelterRepository;
import pro.sky.telegrambot.util.LocationUtil;
import java.util.Map;;
import java.util.concurrent.ConcurrentHashMap;




/**
 * Сервис для обработки логики бота.
 */
@Service
public class BotService {

    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final Keyboard mainMenuKeyboard;
    private static final Map<Long, Integer> incorrectCounts = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger(BotService.class);


    public BotService(TelegramBot telegramBot, ShelterRepository shelterRepository) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
        this.mainMenuKeyboard = new ReplyKeyboardMarkup(
                new String[]{"Выбор приюта", "Инструкция как взять животное из приюта"},
                new String[]{"Прислать отчет о питомце", "Позвать волонтера"},
                new String[]{"Правила безопасности на территории приюта", "Информация о приюте "}

        );
    }
    public void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            long chatId = update.message().chat().id();
            boolean understood = false;
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
                case "Прислать отчет о питомце":
                    sendPetReport(chatId);
                    understood = true;
                    break;
                case "Позвать волонтера":
                    callVolunteer(chatId, text);
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
                default:
                    sendMainMenu(chatId);
                    break;
            }

            if (!understood) {
                var count = incorrectCounts.getOrDefault(chatId, 0);
                if (count < 1) {
                    incorrectCounts.put(chatId, count + 1);
                    writeIncorrectText(chatId, text);
                } else {
                    writeIncorrectText2(chatId, text);
                    incorrectCounts.remove(chatId);
                }
            }
        }
    }

    private void writeIncorrectText(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, "Не понял. Давайте попробуем снова.\n" +
                "Что бы вы хотели сделать? Выберете пункт из /menu");
        telegramBot.execute(message);
    }

    private void writeIncorrectText2(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, "Может тогда вызвать волонтера?");
        telegramBot.execute(message);
        callVolunteer(chatId, text);
    }

    private void informationAboutShelter(long chatId)  {
        SendMessage message = new SendMessage(chatId, "Завести питомца — это очень серьезный шаг и здесь необходимо всё обдумать наперед!\n" +
                "Мы приют животных из Астаны, и в данном разделе меню, ты можешь найти необходимую информацию о нас.").replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Информация о приюте для кошек", "Информация о приюте для собак"},
                new String[]{"Главное меню"}
        ));
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
        telegramBot.execute(new SendMessage(chatId, "Инструкция как взять животное из приюта: ..."));
    }

    private void sendPetReport(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Прислать отчет о питомце: ..."));
    }

    private void callVolunteer(long chatId, String text) {
        try {
            // Отправляем сообщение пользователю
            telegramBot.execute(new SendMessage(chatId, "Запрос отправлен волонтеру."));

            // Отправляем сообщение волонтеру
            sendVolunteer(chatId, text);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения пользователю", e);
            telegramBot.execute(new SendMessage(chatId, "Произошла ошибка при обработке запроса."));
        }
    }

    private void sendVolunteer(long chatId, String text) throws TelegramApiException {
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
