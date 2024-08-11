package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.service.BotService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pro.sky.telegrambot.repository.ClientRepository;

/**
 * Сервис для обработки обновлений от Telegram бота.
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private final TelegramBot telegramBot;
    private final BotService botService;

    /**
     * Внедрение взаимосвязи хранилища данных клиента с Телеграм ботом
     */
    private final ClientRepository clientRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, BotService botService, ClientRepository clientRepository) {
        this.telegramBot = telegramBot;
        this.botService = botService;
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    int startCount = 0;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

                    /**
                     * Первичное и повторное приветствия клиента
                     */
                    logger.info("Processing update: {}", update);
                    Long chatId = update.message().chat().id();
                    if (update.message().text().equals("/start") && startCount == 0) {
                        SendMessage message = new SendMessage(chatId, String.format("Добро пожаловать, %s!\nВас приветствует телеграм-бот Шарик U^ｪ^U\nЯ помогу вам во взаимодействии с приютом для животных и отвечу на самые часто задаваемые вопросы", update.message().from().firstName()));
                        telegramBot.execute(message);
                        startCount++;
                    } else if (startCount == 1) {
                        SendMessage message = new SendMessage(chatId, String.format("С возвращением, %s!\nЧто бы вы хотели узнать?", update.message().from().firstName()));
                        telegramBot.execute(message);
                        startCount++;
                    }
                    try {
                        botService.handleUpdate(update);
                    } catch (Exception e) {
                        logger.error("Ошибка при обработке обновления: {}", update, e);
                    }

                    /**
                     * Проверка корректности персональных данных для связи, введенных клиентом
                     */
                    Pattern pattern = Pattern.compile("([A-Za-z]{2,}[\\-])(\\s)([+0-9]{12})");
                    //Pattern pattern = Pattern.compile("([\\W+])(\\s)([\\W+])(\\s)([0-9\\+]{12})");
                    Matcher matcher = pattern.matcher(update.message().text());
                    String firstName = null;
                    String lastName = null;
                    String phoneNumber = null;
                    if (update.message().text().equals("sendUserContactsMenu")) {
                        if (matcher.matches()) {
                            firstName = matcher.group(1);
                            lastName = matcher.group(1);
                            phoneNumber = matcher.group(3);
                            logger.info("имя: {}, фамилия: {}, телефон:{}", firstName, lastName, phoneNumber);
                        }
                        if (phoneNumber != null && phoneNumber.length() == 12) {
                            clientRepository.save(new Client(chatId, firstName, lastName, phoneNumber));
                            SendMessage message = new SendMessage(chatId, "Ваши данные записаны. Скоро с Вами свяжется волонтер");
                            telegramBot.execute(message);
                        } else {
                            SendMessage message = new SendMessage(chatId, "Неверный формат данных! Попробуйте еще раз");
                            telegramBot.execute(message);
                        }
                    }
                }
        );
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
