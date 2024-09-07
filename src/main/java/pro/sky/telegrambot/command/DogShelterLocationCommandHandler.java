package pro.sky.telegrambot.command;

import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.service.BotService;
import pro.sky.telegrambot.util.LocationUtil;

/**
 * Обработчик команды "Местоположение приюта для собак".
 */
public class DogShelterLocationCommandHandler implements CommandHandler {
    /**
     * Отправляет местоположение приюта для собак.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        LocationUtil.sendDogShelterLocation(chatId, AnimalType.DOG);
    }
}
