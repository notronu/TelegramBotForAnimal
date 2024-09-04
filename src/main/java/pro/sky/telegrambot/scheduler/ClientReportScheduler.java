package pro.sky.telegrambot.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.service.VolunteerService2;

@Component
public class ClientReportScheduler {

    private final VolunteerService2 volunteerService2;
    private final TelegramBot telegramBot;

    @Autowired
    public ClientReportScheduler(VolunteerService2 volunteerService2, TelegramBot telegramBot) {
        this.volunteerService2 = volunteerService2;
        this.telegramBot = telegramBot;
    }

    @Scheduled(fixedRate = 60000) // Запуск каждые 60 секунд
    public void requestReports() {
        List<Client> clients = volunteerService2.getClientsAwaitingReports();
        for (Client client : clients) {
            telegramBot.execute(new SendMessage(client.getChatId(), "Пожалуйста, отправьте отчет с фотографией."));
        }
    }
}
