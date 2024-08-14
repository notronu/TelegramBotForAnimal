package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды по умолчанию.
 */
public class DefaultCommandHandler implements CommandHandler {
    /**
     * Отправляет главное меню.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendMainMenu(chatId);
    }
}