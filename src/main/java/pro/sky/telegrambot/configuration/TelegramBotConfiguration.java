package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки Telegram бота.
 */
@Configuration
public class TelegramBotConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotConfiguration.class);
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Создает экземпляр TelegramBot с заданным токеном.
     * @return объект TelegramBot
     */
    @Bean
    public TelegramBot telegramBot() {
        logger.info("Creating TelegramBot with token: {}", token);
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

}