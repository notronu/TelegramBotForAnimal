package pro.sky.telegrambot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Класс конфигурации для хранения свойств Telegram бота.
 */
@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {
    private String token;

    /**
     * Получает токен Telegram бота.
     * @return токен
     */
    public String getToken() {
        return token;
    }

    /**
     * Устанавливает токен Telegram бота.
     * @param token токен
     */
    public void setToken(String token) {
        this.token = token;
    }
}