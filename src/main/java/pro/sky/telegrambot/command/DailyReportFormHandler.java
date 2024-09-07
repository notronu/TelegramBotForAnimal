package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Форма ежедневного отчета".
 */
public class DailyReportFormHandler implements CommandHandler {
    /**
     * Отправляет форму ежедневного отчета.
     *
     * @param botService сервис для взаимодействия с ботом
     * @param chatId     идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendDailyReportForm(chatId);
    }
}
