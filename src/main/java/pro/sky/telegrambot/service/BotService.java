package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.repository.ShelterRepository;
//import pro.sky.telegrambot.util.LocationUtil;

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
                    //LocationUtil.sendCatShelterLocation(chatId, AnimalType.CAT);
                    break;
                case "Информация о приюте для собак":
                    sendDogShelterInfo(chatId);
                    break;
                case "Местоположение приюта для собак":
                    //LocationUtil.sendDogShelterLocation(chatId, AnimalType.DOG);
                    break;
                case "Связаться со мной":
                    sendUserContactsMenu(chatId);
                    break;
                case "Контактные данные охраны для оформления пропуска на машину в приют для кошек":
                    sendSecurityContactsCatShelterMenu(chatId);
                    break;
                case "Контактные данные охраны для оформления пропуска на машину в приют для собак":
                    sendSecurityContactsDogShelterMenu(chatId);
                    break;
                default:
                    sendMainMenu(chatId);
            }
        }
    }

    private void sendMainMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Выберите меню:")
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

    /**
     * Меню приюта для собак
     * @param chatId
     */
    private void sendDogShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Вас приветствует приют для собак!")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте", "Местоположение приюта для собак"},
                        new String[]{"Контактные данные охраны для оформления пропуска на машину в приют для собак"},
                        new String[]{"Рекомендации по технике безопасности на территории приюта"},
                        new String[]{"Связаться со мной", "Помощь волонтера", "Главное меню"}
                )));
    }

    /**
     * Меню приюта для кошек
     * @param chatId
     */
    private void sendCatShelterInfo(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Вас приветствует приют для кошек!")
        .replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Информация о приюте", "Местоположение приюта для кошек"},
                new String[]{"Контактные данные охраны для оформления пропуска на машину в приют для кошек"},
                new String[]{"Рекомендации по технике безопасности на территории приюта"},
                new String[]{"Связаться со мной", "Помощь волонтера", "Главное меню"}
        )));
    }

    /**
     * Вызов контактных данных для связи с охраной приюта для кошек
     * @param chatId
     */
    private void sendSecurityContactsCatShelterMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Связаться с охраной и оформить пропуск для въезда на территорию приюта вы сможете по телефону:\n+7-999-999-99-01 с 10.00 до 19.00\nбез выходных")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте", "Местоположение приюта для кошек"},
                        new String[]{"Контактные данные охраны для оформления пропуска на машину в приют для кошек"},
                        new String[]{"Рекомендации по технике безопасности на территории приюта"},
                        new String[]{"Связаться со мной", "Помощь волонтера", "Главное меню"}
                )));
    }

    /**
     * Вызов контактных данных для связи с охраной приюта для собак
     * @param chatId
     */
    private void sendSecurityContactsDogShelterMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Связаться с охраной и оформить пропуск для въезда на территорию приюта вы сможете по телефону:\n+7-999-999-99-02 с 9.00 до 18.00\nбез выходных")
                .replyMarkup(new ReplyKeyboardMarkup(
                new String[]{"Информация о приюте", "Местоположение приюта для кошек"},
                new String[]{"Контактные данные охраны для оформления пропуска на машину в приют для кошек"},
                new String[]{"Рекомендации по технике безопасности на территории приюта"},
                new String[]{"Связаться со мной", "Помощь волонтера", "Главное меню"}
        )));
    }

    /**
     * Вызов формы для заполнения контактных данных клиента для консультации связи с волонтером
     * @param chatId
     */
    private void sendUserContactsMenu(long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Если Вы хотите, чтобы с Вами связался волонтер, оставь контактные данные в формате:\nИмя Фамилия номер телефона (без пробелов)\nПример: Иван Иванов +79997775533")
                .replyMarkup(new ReplyKeyboardMarkup(
                        new String[]{"Информация о приюте", "Главное меню"}
                )));
    }
}
