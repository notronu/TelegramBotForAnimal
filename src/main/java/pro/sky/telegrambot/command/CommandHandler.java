package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Интерфейс для обработки команд.
 */
public interface CommandHandler {
    void handle(BotService botService, long chatId);
}