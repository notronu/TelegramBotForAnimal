package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Выбор приюта".
 */
public class ShelterChoiceCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendShelterChoiceMenu(chatId);
    }
}
