package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;

/**
 * Обработчик команды "Информация о приюте для собак".
 */
public class DogShelterInfoCommandHandler implements CommandHandler {
    /**
     * Отправляет информацию о приюте для собак.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        botService.sendDogShelterInfo(chatId);
    }
}