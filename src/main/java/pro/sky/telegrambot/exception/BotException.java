package pro.sky.telegrambot.exception;

/**
 * Класс исключений для бота.
 */
public class BotException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     * @param message сообщение исключения
     */
    public BotException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанным сообщением и причиной.
     * @param message сообщение исключения
     * @param cause причина исключения
     */
    public BotException(String message, Throwable cause) {
        super(message, cause);
    }
}
