package pro.sky.telegrambot.exception;

/**
 * Класс исключений для бота.
 */
public class BotException extends RuntimeException {

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }
}
