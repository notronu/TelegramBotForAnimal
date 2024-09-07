package pro.sky.telegrambot.scheduler;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import pro.sky.telegrambot.model.PetReport;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.VolunteerService2;

import java.util.List;

@Service
public class PetReportScheduler {

    private static final Logger log = LoggerFactory.getLogger(PetReportScheduler.class);
    private final TelegramBot telegramBot;
    private final ReportRepository reportRepository;


    public PetReportScheduler(TelegramBot telegramBot, ReportRepository reportRepository) {
        this.telegramBot = telegramBot;
        this.reportRepository = reportRepository;

    }


    @Scheduled(cron = "0 0 21 * * *")
    public void volunteerRun() {
        final String ADMIN_ID = String.valueOf(194297704);
        try {
            telegramBot.execute(new SendMessage(ADMIN_ID, "Время смотреть отчеты"));
        } catch (RuntimeException e) {
            log.error("ADMIN_ID does`t used");
        }
    }

    @Scheduled(cron = "0 0/59 * * * *")
    public void petReportRun() {
        List<PetReport> petReports = reportRepository.getOwnersAfterTwoDaysReport();
        if(petReports==null) {
            petReports.forEach(e ->
                    telegramBot.execute(new SendMessage(e.getUser().getChatId(), "Нужно прислать отчет")));
        }
    }

}
