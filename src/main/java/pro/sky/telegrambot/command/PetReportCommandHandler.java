package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Прислать отчет о питомце".
 */
public class PetReportCommandHandler implements CommandHandler {
    /**
     * Отправляет отчет о питомце.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendPetReport(chatId);
    }
}
