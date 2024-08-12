package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Информация о приюте для кошек".
 */
public class CatShelterInfoCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendCatShelterInfo(chatId);
    }
}
