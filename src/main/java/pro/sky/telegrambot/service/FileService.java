package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Сервис для отправки файлов.
 */
@Service
public class FileService {

    private final TelegramBot telegramBot;

    public FileService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Отправляет файл в чат.
     * @param chatId идентификатор чата
     * @param filePath путь к файлу
     */
    public void sendFile(long chatId, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            telegramBot.execute(new SendDocument(chatId, file));
        } else {
            telegramBot.execute(new SendMessage(chatId, "Извините, файл не найден."));
        }
    }
}
