package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Неактивен".
 */
public class VolunteerInactiveCommandHandler implements CommandHandler {
    /**
     * Устанавливает волонтера неактивным.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.setVolunteerActive(chatId, false);
    }
}