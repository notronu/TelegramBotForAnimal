package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.VolunteerSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Сервис для управления волонтерами.
 */
@Service
public class VolunteerService {

    private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

    private final TelegramBot telegramBot;
    private final String VOLUNTEER_FILE_PATH = "volunteer_chat_id.txt";
    private final Map<Long, VolunteerSession> volunteerSessions = new HashMap<>();

    public VolunteerService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void registerVolunteer(long chatId) {
        try (FileWriter writer = new FileWriter(VOLUNTEER_FILE_PATH)) {
            writer.write(String.valueOf(chatId));
            volunteerSessions.put(chatId, new VolunteerSession(chatId));
            telegramBot.execute(new SendMessage(chatId, "Вы успешно зарегистрированы как волонтер."));
        } catch (IOException e) {
            logger.error("Error registering volunteer with chatId: " + chatId, e);
            telegramBot.execute(new SendMessage(chatId, "Произошла ошибка при регистрации волонтера."));
        }
    }

    public long getVolunteerChatId() {
        try {
            File file = new File(VOLUNTEER_FILE_PATH);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    if (scanner.hasNextLong()) {
                        return scanner.nextLong();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error reading volunteer chat ID from file.", e);
        }
        return 0; // Возвращает 0, если chat ID не найден
    }

    public void setVolunteerActive(long chatId, boolean active) {
        VolunteerSession session = volunteerSessions.get(chatId);
        if (session != null) {
            session.setActive(active);
            String message = active ? "Вы активны и готовы помогать клиентам." : "Вы неактивны и не будете получать запросы от клиентов.";
            telegramBot.execute(new SendMessage(chatId, message));
        }
    }

    public Map<Long, VolunteerSession> getVolunteerSessions() {
        return volunteerSessions;
    }
}