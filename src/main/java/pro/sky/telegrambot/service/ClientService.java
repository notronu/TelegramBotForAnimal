package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.ClientSession;
import pro.sky.telegrambot.model.VolunteerSession;

import java.util.Map;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final TelegramBot telegramBot;
    private final ChatService chatService;
    private final VolunteerService volunteerService;

    public ClientService(TelegramBot telegramBot, ChatService chatService, VolunteerService volunteerService) {
        this.telegramBot = telegramBot;
        this.chatService = chatService;
        this.volunteerService = volunteerService;
    }

    public void callVolunteer(long clientChatId) {
        Map<Long, VolunteerSession> volunteerSessions = volunteerService.getVolunteerSessions();
        if (volunteerSessions.isEmpty()) {
            telegramBot.execute(new SendMessage(clientChatId, "К сожалению, в настоящее время нет доступных волонтеров."));
        } else {
            ClientSession clientSession = new ClientSession(clientChatId);
            clientSession.setClientName("Клиент"); // Замените на фактическое имя клиента
            clientSession.setClientQuestion("Вопрос клиента"); // Замените на фактический вопрос клиента
            logger.info("Client {} is requesting a volunteer", clientChatId);
            chatService.notifyVolunteer(clientSession, volunteerSessions);
            telegramBot.execute(new SendMessage(clientChatId, "Ваш запрос на волонтера был отправлен. Пожалуйста, подождите."));
        }
    }
}