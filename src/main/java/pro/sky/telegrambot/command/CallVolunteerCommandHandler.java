package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class CallVolunteerCommandHandler implements CommandHandler {
    /**
     * Вызывает волонтера для чата.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.callVolunteer(chatId);
    }
}