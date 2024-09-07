package pro.sky.telegrambot.listener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;
import pro.sky.telegrambot.processor.UpdateProcessor;
import pro.sky.telegrambot.service.BotService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Сервис для обработки обновлений от Telegram бота.
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final BotService botService;
    private final UpdateProcessor updateProcessor;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, BotService botService, UpdateProcessor updateProcessor) {
        this.telegramBot = telegramBot;
        this.botService = botService;
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        logger.info("TelegramBotUpdatesListener initialized and ready to receive updates.");
    }
    @Override
    public int process(List<Update> updates) {
        logger.info("Received {} updates.", updates.size());
        updateProcessor.process(updates);
        logger.info("Finished processing updates.");
        updates.forEach(update -> {

            try {
                botService.handleUpdate(update);
            } catch (Exception e) {
                logger.error("Ошибка при обработке обновления: {}", update, e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }
}




