package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.PetSession;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.model.VolunteerSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import pro.sky.telegrambot.repository.VolunteerRepository;

/**
 * Сервис для управления волонтерами.
 */
@Service
public class VolunteerService {

    private static final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

    private final TelegramBot telegramBot;
    private final VolunteerRepository volunteerRepository;
    private final String VOLUNTEER_FILE_PATH = "volunteer_chat_id.txt";
    private final Map<Long, VolunteerSession> volunteerSessions = new HashMap<>();
    private static final AtomicInteger petCounter = new AtomicInteger(1);
    private final List<PetSession> petSessions = new ArrayList<>();

    public VolunteerService(TelegramBot telegramBot, VolunteerRepository volunteerRepository) {
        this.telegramBot = telegramBot;
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Регистрирует волонтера.
     * @param chatId идентификатор чата волонтера
     */
    public void registerVolunteer(long chatId) {
        try (FileWriter writer = new FileWriter(VOLUNTEER_FILE_PATH)) {
            writer.write(String.valueOf(chatId));
            volunteerSessions.put(chatId, new VolunteerSession(chatId));
            saveVolunteer(chatId);
            telegramBot.execute(new SendMessage(chatId, "Вы успешно зарегистрированы как волонтер."));
        } catch (IOException e) {
            logger.error("Error registering volunteer with chatId: " + chatId, e);
            telegramBot.execute(new SendMessage(chatId, "Произошла ошибка при регистрации волонтера."));
        }
    }

    // Метод для регистрации волонтера
    public Volunteer saveVolunteer(Long chatId) {
        if (isVolunteerRegistered(chatId)) {
            return null;  // Возвращаем null, если волонтер уже зарегистрирован
        }

        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        return volunteerRepository.save(volunteer);
    }

    // Метод для проверки, зарегистрирован ли волонтер
    private boolean isVolunteerRegistered(Long chatId) {
        return volunteerRepository.findByChatId(chatId) != null;
    }

    /**
     * Получает идентификатор чата волонтера из файла.
     * @return идентификатор чата волонтера
     */
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

    /**
     * Устанавливает волонтера активным или неактивным.
     * @param chatId идентификатор чата волонтера
     * @param active статус активности
     */
    public void setVolunteerActive(long chatId, boolean active) {
        VolunteerSession session = volunteerSessions.get(chatId);
        if (session != null) {
            session.setActive(active);
            String message = active ? "Вы активны и готовы помогать клиентам." : "Вы неактивны и не будете получать запросы от клиентов.";
            telegramBot.execute(new SendMessage(chatId, message));
        }
    }

    /**
     * Получает сессии всех волонтеров.
     * @return карта сессий волонтеров
     */
    public Map<Long, VolunteerSession> getVolunteerSessions() {
        return volunteerSessions;
    }

    public int addPet(String name, String breed, String photoPath) {
        int petId = petCounter.getAndIncrement();
        PetSession petSession = new PetSession(petId, name, breed, photoPath);
        petSessions.add(petSession);
        return petId;
    }

    public List<PetSession> getPets() {
        return petSessions;
    }


}
