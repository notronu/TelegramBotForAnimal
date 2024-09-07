package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Регистрация волонтера".
 */
public class RegisterVolunteerCommandHandler implements CommandHandler {
    /**
     * Регистрирует волонтера и отправляет меню волонтера.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.registerVolunteer(chatId);
        botService.sendVolunteerMenu(chatId);
    }
}
