package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class InfoForDisabledPetHandler implements CommandHandler {
    /**
     * Отправляет инструкцию улучшения условий для питомца с ограниченными возможностями.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.getRecommendationsHomeImprovementForDisabledPet(chatId);

    }
}
