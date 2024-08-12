package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Рекомендации по проверенным кинологам".
 */
public class RecommendCynologistsCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendCynologistRecommendations(chatId);
    }
}