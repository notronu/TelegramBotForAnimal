package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.VolunteerSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для класса {@link VolunteerService}.
 * <p>
 * Этот класс проверяет корректность работы методов сервиса, который управляет волонтерскими сессиями,
 * включая регистрацию волонтеров, управление их статусом и сохранение/получение данных о них.
 * Используются mock-объекты для подмены зависимостей {@link TelegramBot}.
 */
class VolunteerServiceTest {

    /**
     * Mock-объект для работы с Telegram API.
     */
    @Mock
    private TelegramBot telegramBot;

    /**
     * Внедрение mock-объектов в тестируемый сервис.
     */
    @InjectMocks
    private VolunteerService volunteerService;

    /**
     * Путь к файлу, где сохраняется информация о волонтерах.
     */
    private final String VOLUNTEER_FILE_PATH = "volunteer_chat_id.txt";

    /**
     * Настройка окружения перед каждым тестом. Инициализирует mock-объекты.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Проверяет корректность регистрации волонтера и сохранения его ID в файл.
     * Тест также проверяет, что соответствующее сообщение отправляется через TelegramBot.
     *
     * @throws IOException если возникает ошибка при работе с файлом.
     */
    @Test
    void registerVolunteer_shouldRegisterVolunteerAndSaveChatId() throws IOException {
        long chatId = 12345L;

        volunteerService.registerVolunteer(chatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(1)).execute(captor.capture());

        SendMessage capturedMessage = captor.getValue();
        assertEquals(chatId, capturedMessage.getParameters().get("chat_id"));
        assertEquals("Вы успешно зарегистрированы как волонтер.", capturedMessage.getParameters().get("text"));

        assertTrue(volunteerService.getVolunteerSessions().containsKey(chatId), "Volunteer session should be registered.");
        assertTrue(new File(VOLUNTEER_FILE_PATH).exists(), "File should exist after registration.");

        // Cleanup
        new File(VOLUNTEER_FILE_PATH).delete();
    }

    /**
     * Проверяет обработку исключения IOException при регистрации волонтера.
     * Если файл для сохранения ID волонтера недоступен, волонтер получает сообщение об ошибке.
     */
    @Test
    void registerVolunteer_shouldHandleIOException() {
        long chatId = 12345L;
        File volunteerFile = new File(VOLUNTEER_FILE_PATH);

        try {
            if (volunteerFile.exists()) {
                volunteerFile.delete(); // Удаляем файл, если он существует, чтобы создать заново
            }
            // Создаем файл и устанавливаем права только на чтение
            volunteerFile.createNewFile();
            volunteerFile.setReadOnly();

            volunteerService.registerVolunteer(chatId);

            ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
            verify(telegramBot, times(1)).execute(captor.capture());

            SendMessage capturedMessage = captor.getValue();
            assertEquals(chatId, capturedMessage.getParameters().get("chat_id"));
            assertEquals("Произошла ошибка при регистрации волонтера.", capturedMessage.getParameters().get("text"));

        } catch (IOException e) {
            fail("Unexpected IOException during test setup");
        } finally {
            // Cleanup
            volunteerFile.setWritable(true);
            volunteerFile.delete();
        }
    }

    /**
     * Проверяет получение ID чата волонтера из файла.
     * Если файл существует, метод должен вернуть ID, записанный в файл.
     *
     * @throws IOException если возникает ошибка при работе с файлом.
     */
    @Test
    void getVolunteerChatId_shouldReturnChatIdFromFile() throws IOException {
        long expectedChatId = 12345L;
        try (FileWriter writer = new FileWriter(VOLUNTEER_FILE_PATH)) {
            writer.write(String.valueOf(expectedChatId));
        }

        long actualChatId = volunteerService.getVolunteerChatId();

        assertEquals(expectedChatId, actualChatId);

        // Cleanup
        new File(VOLUNTEER_FILE_PATH).delete();
    }

    /**
     * Проверяет, что метод возвращает 0, если файл с ID волонтера не найден.
     * Это может означать, что волонтер еще не зарегистрирован.
     */
    @Test
    void getVolunteerChatId_shouldReturnZeroWhenFileNotFound() {
        long actualChatId = volunteerService.getVolunteerChatId();

        assertEquals(0L, actualChatId);
    }

    /**
     * Проверяет установку статуса активности волонтера и отправку соответствующего сообщения.
     * Волонтер становится активным, и ему отправляется сообщение через TelegramBot.
     */
    @Test
    void setVolunteerActive_shouldSetVolunteerStatusAndSendMessage() {
        long chatId = 12345L;
        VolunteerSession session = new VolunteerSession(chatId);
        volunteerService.getVolunteerSessions().put(chatId, session);

        volunteerService.setVolunteerActive(chatId, true);

        assertTrue(session.isActive(), "Volunteer session should be active.");

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(1)).execute(captor.capture());

        SendMessage capturedMessage = captor.getValue();
        assertEquals(chatId, capturedMessage.getParameters().get("chat_id"));
        assertEquals("Вы активны и готовы помогать клиентам.", capturedMessage.getParameters().get("text"));
    }

    /**
     * Проверяет возврат всех активных сессий волонтеров.
     * Метод должен возвращать карту сессий, содержащую всех зарегистрированных волонтеров.
     */
    @Test
    void getVolunteerSessions_shouldReturnAllVolunteerSessions() {
        long chatId1 = 12345L;
        long chatId2 = 67890L;
        volunteerService.getVolunteerSessions().put(chatId1, new VolunteerSession(chatId1));
        volunteerService.getVolunteerSessions().put(chatId2, new VolunteerSession(chatId2));

        Map<Long, VolunteerSession> sessions = volunteerService.getVolunteerSessions();

        assertEquals(2, sessions.size());
        assertTrue(sessions.containsKey(chatId1));
        assertTrue(sessions.containsKey(chatId2));
    }
}