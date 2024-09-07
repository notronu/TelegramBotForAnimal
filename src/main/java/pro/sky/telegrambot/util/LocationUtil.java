package pro.sky.telegrambot.util;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.AnimalType;

/**
 * Утилита для работы с локациями.
 */
@Component
public class LocationUtil {

    private static TelegramBot telegramBot;

    @Autowired
    public LocationUtil(TelegramBot telegramBot) {
        LocationUtil.telegramBot = telegramBot;
    }

    /**
     * Отправляет ссылку на местоположение приюта для кошек.
     * @param chatId идентификатор чата
     * @param type тип животных
     */
    public static void sendCatShelterLocation(long chatId, AnimalType type) {
        String locationUrl = "https://www.google.com/maps/dir/?api=1&destination=координаты_приюта_для_кошек";
        telegramBot.execute(new SendMessage(chatId, "Местоположение приюта для кошек: " + locationUrl));
    }

    /**
     * Отправляет ссылку на местоположение приюта для собак.
     * @param chatId идентификатор чата
     * @param type тип животных
     */
    public static void sendDogShelterLocation(long chatId, AnimalType type) {
        String locationUrl = "https://www.google.com/maps/dir/?api=1&destination=координаты_приюта_для_собак";
        telegramBot.execute(new SendMessage(chatId, "Местоположение приюта для собак: " + locationUrl));
    }
}
