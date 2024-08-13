package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Неактивен".
 */
public class VolunteerInactiveCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.setVolunteerActive(chatId, false);
    }
}