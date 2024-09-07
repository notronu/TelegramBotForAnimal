package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class PetReportMenuHandler implements CommandHandler {
    /**
     * Отправляет меню инструкций по отчету.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendReportMenu(chatId);
    }
}
