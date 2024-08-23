package pro.sky.telegrambot.listener;



import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;

import pro.sky.telegrambot.repository.ReportRepository;




@Service
public class Notification {

    private final TelegramBot telegramBot;
    private final ReportRepository reportRepository;

    public Notification(TelegramBot telegramBot, ReportRepository reportRepository) {
        this.telegramBot = telegramBot;
        this.reportRepository = reportRepository;
    }


}
