package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final TelegramBot telegramBot;
    private final Map<Long, Long> activeChats = new HashMap<>();
    private final Map<Long, Long> clientVolunteerMap = new HashMap<>();
    private final Map<Long, ClientSession> clientSessions = new HashMap<>();
    private final VolunteerService volunteerService;

    public ChatService(TelegramBot telegramBot, VolunteerService volunteerService) {
        this.telegramBot = telegramBot;
        this.volunteerService = volunteerService;
    }

    /**
     * Проверяет, является ли чат активным.
     * @param chatId идентификатор чата
     * @return true, если чат активен, иначе false
     */
    public boolean isActiveChat(long chatId) {
        return activeChats.containsKey(chatId) || activeChats.containsValue(chatId);
    }

    /**
     * Направляет сообщение от одного участника чата к другому.
     * @param senderChatId идентификатор чата отправителя
     * @param message сообщение
     */
    public void routeMessage(long senderChatId, Message message) {
        Long recipientChatId = activeChats.get(senderChatId);
        if (recipientChatId == null) {
            recipientChatId = activeChats.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(senderChatId))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(0L);
        }

        if (recipientChatId != 0) {
            telegramBot.execute(new SendMessage(recipientChatId, message.text()));
        }
    }

    /**
     * Начинает чат между клиентом и волонтером.
     * @param clientChatId идентификатор чата клиента
     * @param volunteerChatId идентификатор чата волонтера
     */
    public void startChat(long clientChatId, long volunteerChatId) {
        logger.info("Starting chat between client {} and volunteer {}", clientChatId, volunteerChatId);
        activeChats.put(clientChatId, volunteerChatId);
        clientSessions.put(clientChatId, new ClientSession(clientChatId));
        clientVolunteerMap.put(clientChatId, volunteerChatId); // Добавляем соответствие клиента и волонтера
        VolunteerSession volunteerSession = volunteerService.getVolunteerSessions().get(volunteerChatId);
        if (volunteerSession != null) {
            volunteerSession.setBusy(true);
        }
    }

    /**
     * Завершает чат.
     * @param chatId идентификатор чата
     */
    public void endChat(long chatId) {
        logger.info("Ending chat for {}", chatId);
        clientVolunteerMap.remove(chatId);
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
                clientVolunteerMap.remove(clientChatId);
                Long volunteerId = activeChats.remove(clientChatId);
                if (volunteerId != null) {
                    volunteerService.getVolunteerSessions().get(volunteerId).setBusy(false);
                }
                clientSessions.remove(clientChatId);
                telegramBot.execute(new SendMessage(clientChatId, "Волонтер покинул беседу."));
            }
        }
    }

    /**
     * Получает идентификатор чата клиента для указанного волонтера.
     * @param volunteerChatId идентификатор чата волонтера
     * @return идентификатор чата клиента
     */
    public long getClientChatIdForVolunteer(long volunteerChatId) {
        logger.info("Getting client chat ID for volunteer {}", volunteerChatId);
        long clientChatId = clientVolunteerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(volunteerChatId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0L);
        logger.info("Found client chat ID: {}", clientChatId);
        return clientChatId;
    }

    /**
     * Уведомляет волонтера о новом клиенте.
     * @param clientSession сессия клиента
     * @param volunteerSessions сессии волонтеров
     */
    public void notifyVolunteer(ClientSession clientSession, Map<Long, VolunteerSession> volunteerSessions) {
        for (VolunteerSession volunteerSession : volunteerSessions.values()) {
            if (!volunteerSession.isBusy() && volunteerSession.isActive()) {
                logger.info("Notifying volunteer {} about client {}", volunteerSession.getChatId(), clientSession.getChatId());
                clientVolunteerMap.put(clientSession.getChatId(), volunteerSession.getChatId());  // Добавляем в активные чаты
                telegramBot.execute(new SendMessage(volunteerSession.getChatId(), "Клиент " + clientSession.getChatId() + " просит ответить на вопросы.")
                        .replyMarkup(new ReplyKeyboardMarkup(
                                new String[]{"Присоединиться к беседе с клиентом", "Главное меню"}
                        )));
                return;  // Уведомили одного свободного волонтера и вышли из метода
            }
        }
        // Если нет свободных волонтеров
        logger.info("No available volunteers for client {}", clientSession.getChatId());
        telegramBot.execute(new SendMessage(clientSession.getChatId(), "К сожалению, в настоящее время нет доступных волонтеров."));
    }
}