package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Получить файл".
 */
public class GetFileCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendFile(chatId, "/path/to/file.doc");
    }
}