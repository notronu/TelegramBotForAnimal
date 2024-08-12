package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Позвать волонтера".
 */
public class CallVolunteerCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.callVolunteer(chatId);
    }
}