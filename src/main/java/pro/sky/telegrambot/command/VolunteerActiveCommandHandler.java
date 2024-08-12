package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Активен".
 */
public class VolunteerActiveCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.setVolunteerActive(chatId, true);
    }
}