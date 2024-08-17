package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.ClientSession;
import pro.sky.telegrambot.model.VolunteerSession;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для класса {@link ClientService}.
 * <p>
 * Этот класс проверяет корректность работы методов сервиса, который взаимодействует с клиентами,
 * включая вызов волонтера и отправку сообщений через TelegramBot.
 * Используются mock-объекты для подмены зависимостей {@link TelegramBot}, {@link ChatService}, и {@link VolunteerService}.
 */
class ClientServiceTest {

    /**
     * Mock-объект для работы с Telegram API.
     */
    @Mock
    private TelegramBot telegramBot;

    /**
     * Mock-объект для работы с сервисом чатов.
     */
    @Mock
    private ChatService chatService;

    /**
     * Mock-объект для работы с волонтерским сервисом.
     */
    @Mock
    private VolunteerService volunteerService;

    /**
     * Внедрение mock-объектов в тестируемый сервис.
     */
    @InjectMocks
    private ClientService clientService;

    /**
     * Настройка окружения перед каждым тестом. Инициализирует mock-объекты.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Проверяет сценарий, когда нет доступных волонтеров для ответа клиенту.
     * Клиент получает сообщение о том, что волонтеры недоступны.
     */
    @Test
    void testCallVolunteer_noVolunteersAvailable() {
        long clientChatId = 12345L;
        when(volunteerService.getVolunteerSessions()).thenReturn(new HashMap<>());

        clientService.callVolunteer(clientChatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(clientChatId, (long) actualMessage.getParameters().get("chat_id"));
        assertEquals("К сожалению, в настоящее время нет доступных волонтеров.", actualMessage.getParameters().get("text"));
    }

    /**
     * Проверяет сценарий, когда доступен волонтер для ответа клиенту.
     * Клиент получает сообщение о том, что его запрос обработан, и волонтер уведомлен.
     */
    @Test
    void testCallVolunteer_volunteerAvailable() {
        long clientChatId = 12345L;
        long volunteerChatId = 67890L;

        VolunteerSession volunteerSession = new VolunteerSession(volunteerChatId);
        Map<Long, VolunteerSession> volunteerSessions = Map.of(volunteerChatId, volunteerSession);

        when(volunteerService.getVolunteerSessions()).thenReturn(volunteerSessions);

        clientService.callVolunteer(clientChatId);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(messageCaptor.capture());

        // Проверяет сообщение клиенту
        SendMessage messageToClient = messageCaptor.getValue();
        assertEquals(clientChatId, (long) messageToClient.getParameters().get("chat_id"));
        assertEquals("Ваш запрос на волонтера был отправлен. Пожалуйста, подождите.", messageToClient.getParameters().get("text"));

        // Проверяет, что метод notifyVolunteer был вызван в chatService
        ArgumentCaptor<ClientSession> clientSessionCaptor = ArgumentCaptor.forClass(ClientSession.class);
        verify(chatService).notifyVolunteer(clientSessionCaptor.capture(), eq(volunteerSessions));

        ClientSession capturedClientSession = clientSessionCaptor.getValue();
        assertEquals(clientChatId, capturedClientSession.getChatId());
        assertEquals("Клиент", capturedClientSession.getClientName());
        assertEquals("Вопрос клиента", capturedClientSession.getClientQuestion());
    }
}