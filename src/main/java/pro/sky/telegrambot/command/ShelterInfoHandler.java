package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Информация о приюте ".
 */
public class ShelterInfoHandler implements CommandHandler {
    /**
     * Отправляет информацию о приюте.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.informationAboutShelter(chatId);
    }
}
