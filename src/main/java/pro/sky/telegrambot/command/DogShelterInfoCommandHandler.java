package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Информация о приюте для собак".
 */
public class DogShelterInfoCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendDogShelterInfo(chatId);
    }
}