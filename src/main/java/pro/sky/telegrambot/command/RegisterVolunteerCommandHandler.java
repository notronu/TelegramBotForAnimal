package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Регистрация волонтера".
 */
public class RegisterVolunteerCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.registerVolunteer(chatId);
    }
}