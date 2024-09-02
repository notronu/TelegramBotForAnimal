package pro.sky.telegrambot.check;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import pro.sky.telegrambot.model.Photo;
import pro.sky.telegrambot.repository.PhotoRepository;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class CheckReport {
    private static final Logger log = LoggerFactory.getLogger(CheckReport.class);
    private final TelegramBot telegramBot;
    private final PhotoRepository photoRepository;

    public CheckReport(TelegramBot telegramBot, PhotoRepository photoRepository) {
        this.telegramBot = telegramBot;
        this.photoRepository = photoRepository;
    }

    @Scheduled(cron = "0 0 21   ") // проверка в 9 вечера
    public void checkReport() {
        List<Photo> photoList = photoRepository.findPhotoByDate();
        if (photoList != null) {
            photoList.forEach(photo -> {

                // Получение chatId из объекта Photo
                Long chatId = photo.getChatId();
                String fileId = photo.getFileId();
                String text = photo.getText();
                if (chatId != null) {
                    String text1 = "Нужно прислать отчет";
                    photo.execute(new SendMessage(String.valueOf(chatId), text1));
                    log.info("Напоминание об отправке отчетов направлено");
                } else {
                    log.info("Все отчеты получены");
                }
            });

        }
    }
}




