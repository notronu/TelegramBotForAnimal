package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Активен".
 */
public class VolunteerActiveCommandHandler implements CommandHandler {
    /**
     * Устанавливает волонтера активным.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.setVolunteerActive(chatId, true);
    }
}