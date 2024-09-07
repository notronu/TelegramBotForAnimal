package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class ReasonsForRefuseHandler implements CommandHandler {
    /**
     * Отправляет информацию о возможноом отказе.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.getReasonsForRefusal(chatId);

    }
}
