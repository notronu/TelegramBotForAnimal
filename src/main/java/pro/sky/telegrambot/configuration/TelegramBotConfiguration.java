package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки Telegram бота.
 */
@Configuration
public class TelegramBotConfiguration {




    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Создает экземпляр TelegramBot с заданным токеном.
     * @return объект TelegramBot
     */
    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(token);
    }

}