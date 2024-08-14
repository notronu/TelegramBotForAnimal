package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Интерфейс для обработки команд.
 */
public interface CommandHandler {
    /**
     * Обрабатывает команду.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    void handle(BotService botService, long chatId);
}