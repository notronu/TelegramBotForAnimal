package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Рекомендации по проверенным кинологам".
 */
public class RecommendCynologistsCommandHandler implements CommandHandler {
    /**
     * Отправляет рекомендации по проверенным кинологам.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendCynologistRecommendations(chatId);
    }
}