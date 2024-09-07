package pro.sky.telegrambot.command;

import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.service.BotService;
import pro.sky.telegrambot.util.LocationUtil;

/**
 * Обработчик команды "Местоположение приюта для кошек".
 */
public class CatShelterLocationCommandHandler implements CommandHandler {
    /**
     * Отправляет местоположение приюта для кошек.
     * @param botService сервис для взаимодействия с ботом
     * @param chatId идентификатор чата
     */
    @Override
    public void handle(BotService botService, long chatId) {
        LocationUtil.sendCatShelterLocation(chatId, AnimalType.CAT);
    }
}
