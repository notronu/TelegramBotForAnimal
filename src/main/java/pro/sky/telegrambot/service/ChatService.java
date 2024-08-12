package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.ClientSession;
import pro.sky.telegrambot.model.VolunteerSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления активными чатами между волонтерами и клиентами.
 */
@Service
public class ChatService {

    private final TelegramBot telegramBot;
    private final Map<Long, Long> activeChats = new HashMap<>();
    private final Map<Long, ClientSession> clientSessions = new HashMap<>();
    private final VolunteerService volunteerService;

    public ChatService(TelegramBot telegramBot, VolunteerService volunteerService) {
        this.telegramBot = telegramBot;
        this.volunteerService = volunteerService;
    }

    public boolean isActiveChat(long chatId) {
        return activeChats.containsKey(chatId) || activeChats.containsValue(chatId);
    }

    public void routeMessage(long senderChatId, Message message) {
        long recipientChatId = activeChats.getOrDefault(senderChatId, activeChats.entrySet().stream()
                .filter(entry -> entry.getValue().equals(senderChatId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0L));

        if (recipientChatId != 0) {
            telegramBot.execute(new SendMessage(recipientChatId, message.text()));
        }
    }

    public void startChat(long clientChatId, long volunteerChatId) {
        activeChats.put(clientChatId, volunteerChatId);
        clientSessions.put(clientChatId, new ClientSession(clientChatId));
        VolunteerSession volunteerSession = volunteerService.getVolunteerSessions().get(volunteerChatId);
        if (volunteerSession != null) {
            volunteerSession.setBusy(true);
        }
    }

    public void endChat(long chatId) {
        Long volunteerChatId = activeChats.remove(chatId);
        if (volunteerChatId != null) {
            volunteerService.getVolunteerSessions().get(volunteerChatId).setBusy(false);
            telegramBot.execute(new SendMessage(volunteerChatId, "Вы покинули беседу."));
        } else {
            Long clientChatId = activeChats.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(chatId))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            if (clientChatId != null) {
                Long volunteerId = activeChats.remove(clientChatId);
                if (volunteerId != null) {
                    volunteerService.getVolunteerSessions().get(volunteerId).setBusy(false);
                }
                clientSessions.remove(clientChatId);
                telegramBot.execute(new SendMessage(clientChatId, "Волонтер покинул беседу."));
            }
        }
    }

    public void notifyVolunteer(ClientSession clientSession, Map<Long, VolunteerSession> volunteerSessions) {
        for (VolunteerSession volunteerSession : volunteerSessions.values()) {
            if (!volunteerSession.isBusy() && volunteerSession.isActive()) {
                telegramBot.execute(new SendMessage(volunteerSession.getChatId(), "Клиент " + clientSession.getChatId() + " просит ответить на вопросы.")
                        .replyMarkup(new ReplyKeyboardMarkup(
                                new String[]{"Присоедениться к беседе с клиентом", "Главное меню"}
                        )));
                return;  // Уведомили одного свободного волонтера и вышли из метода
            }
        }
        // Если нет свободных волонтеров
        telegramBot.execute(new SendMessage(clientSession.getChatId(), "К сожалению, в настоящее время нет доступных волонтеров."));
    }
}