package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

public class PetTransportationHandler implements CommandHandler {
    /**
     * Отправляет информацию о транспортировке питомца.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.getRecommendationsAnimalTransportation(chatId);

    }
}
