package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class CommandHandler {

    private final TelegramBot telegramBot;

    public CommandHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handleCommand(Long chatId, String command) {
        switch (command) {
            case "информация о приюте":
                sendInfoMessage(chatId);
                break;
            case "Как взять животное из приюта":
                sendAdoptionInfo(chatId);
                break;
            case "Инструкции по отправке отчета о питомце":
                sendReportInstructions(chatId);
                break;
            case "позвать волонтера":
                callVolunteer(chatId);
                break;
            default:
                sendDefaultMessage(chatId);
                break;
        }
    }

    private void sendInfoMessage(Long chatId) {
        String infoMessage = "Информация о приюте...";
        telegramBot.execute(new SendMessage(chatId, infoMessage));
    }

    private void sendAdoptionInfo(Long chatId) {
        String adoptionMessage = "Как взять животное из приюта...";
        telegramBot.execute(new SendMessage(chatId, adoptionMessage));
    }

    private void sendReportInstructions(Long chatId) {
        String reportMessage = "Инструкции по отправке отчета о питомце...";
        telegramBot.execute(new SendMessage(chatId, reportMessage));
    }

    private void callVolunteer(Long chatId) {
        String volunteerMessage = "Волонтер скоро свяжется с вами.";
        telegramBot.execute(new SendMessage(chatId, volunteerMessage));
    }

    private void sendDefaultMessage(Long chatId) {
        String defaultMessage = "Извините, я не понимаю эту команду.";
        telegramBot.execute(new SendMessage(chatId, defaultMessage));
    }
}
