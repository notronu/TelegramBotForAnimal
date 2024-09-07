package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Выбор приюта".
 */
public class ShelterChoiceCommandHandler implements CommandHandler {
    /**
     * Отправляет меню выбора приюта.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendShelterChoiceMenu(chatId);
    }
}
