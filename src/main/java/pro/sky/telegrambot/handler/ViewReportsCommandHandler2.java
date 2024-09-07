package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.PetReport;
import pro.sky.telegrambot.service.VolunteerService2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class ViewReportsCommandHandler2 implements CommandHandler2 {

    private final Logger logger = LoggerFactory.getLogger(ViewReportsCommandHandler2.class);
    private final VolunteerService2 volunteerService2;
    private final TelegramBot telegramBot;
    private final PetReport petReport;

    private final Map<Long, Integer> userCurrentReportIndex = new HashMap<>();

    @Autowired
    public ViewReportsCommandHandler2(VolunteerService2 volunteerService2, TelegramBot telegramBot, PetReport petReport) {
        this.volunteerService2 = volunteerService2;
        this.telegramBot = telegramBot;
        this.petReport = petReport;
    }

    @Override
    public boolean canHandle(String command) {
        return command != null && (command.equals("Посмотреть отчеты") || command.startsWith("view_report") || command.startsWith("approve_report") || command.startsWith("disapprove_report"));
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.chat().id();
        showReport(chatId, 0);  // Начинаем с первого питомца
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.message().chat().id();
        String data = callbackQuery.data();

        if (data.startsWith("view_report")) {
            int reportIndex = Integer.parseInt(data.split(":")[1]);
            showReport(chatId, reportIndex);
        } else if (data.startsWith("approve_report")) {
            int reportIndex = Integer.parseInt(data.split(":")[1]);
            approveReport(chatId, reportIndex);
        } else if (data.startsWith("disapprove_report")) {
            int reportIndex = Integer.parseInt(data.split(":")[1]);
            disApproveReport(chatId, reportIndex);
        }
    }

    private void showReport(Long chatId, int reportIndex) {
        List<PetReport> petReports = volunteerService2.getReportByVolunteer(chatId);

        if (petReports.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "Нет отчетов."));
            return;
        }

        if (reportIndex < 0 || reportIndex >= petReports.size()) {
            telegramBot.execute(new SendMessage(chatId, "Вы посмотрели все отчеты."));
            return;
        }

        PetReport petReport = petReports.get(reportIndex);
        userCurrentReportIndex.put(chatId, reportIndex);

        String reportInfo = String.format("Питомец %d из %d\n\nКличка: %s\nРацион: %s, %s\nСамочувствие: %s, %s\nПривычки: %s",
                reportIndex + 1, petReports.size(), petReport.getName(), petReport.getAnimalsDiet(), petReport.getAnimalHealth(), petReport.getAnimalHabits());

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Отчет одобрен").callbackData("approve_report:" + reportIndex),
                new InlineKeyboardButton("Отчет не одобрен").callbackData("disapprove_report:" + reportIndex),
                new InlineKeyboardButton("Далее").callbackData("view_report:" + (reportIndex + 1))
        );

        // Отправка фото питомца
        if (petReport.getPhotoFileId() != null) {
            SendPhoto sendPhoto = new SendPhoto(chatId, petReport.getPhotoFileId())
                    .caption(reportInfo)
                    .replyMarkup(keyboard);
            telegramBot.execute(sendPhoto);
        } else {
            SendMessage message = new SendMessage(chatId, reportInfo)
                    .replyMarkup(keyboard);
            telegramBot.execute(message);
        }

        logger.info("Showing pet index {} to chatId: {}", reportIndex, chatId);
    }

    private void approveReport(Long chatId, int reportIndex) {
        List<PetReport> petReports = volunteerService2.getReportByVolunteer(chatId);
        telegramBot.execute(new SendMessage(chatId, "Отчет одобрен"));
    }

    private void disApproveReport(Long chatId, int reportIndex) {
        List<PetReport> petReports = volunteerService2.getReportByVolunteer(chatId);
        telegramBot.execute(new SendMessage(chatId, "Отчет не одобрен"));

    }
}


