package pro.sky.telegrambot.command;

import pro.sky.telegrambot.service.BotService;
import pro.sky.telegrambot.model.AnimalType;
import pro.sky.telegrambot.util.LocationUtil;

/**
 * Обработчик команды "Местоположение приюта для кошек".
 */
public class CatShelterLocationCommandHandler implements CommandHandler {
    @Override
    public void handle(BotService botService, long chatId) {
        LocationUtil.sendCatShelterLocation(chatId, AnimalType.CAT);
    }
}