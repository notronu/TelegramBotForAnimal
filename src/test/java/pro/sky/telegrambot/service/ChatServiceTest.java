package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambot.model.ClientSession;
import pro.sky.telegrambot.model.VolunteerSession;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для класса {@link ChatService}.
 * <p>
 * Этот класс проверяет корректность работы методов сервиса, управляющего чатом между клиентами и волонтерами,
 * включая маршрутизацию сообщений, начало и завершение чатов, а также уведомление волонтеров.
 * Используются mock-объекты для подмены зависимостей {@link TelegramBot} и {@link VolunteerService}.
 */
class ChatServiceTest {

    /**
     * Mock-объект для работы с Telegram API.
     */
    @Mock
    private TelegramBot telegramBot;

    /**
     * Mock-объект для работы с волонтерским сервисом.
     */
    @Mock
    private VolunteerService volunteerService;

    /**
     * Внедрение mock-объектов в тестируемый сервис.
     */
    @InjectMocks
    private ChatService chatService;

    /**
     * Настройка окружения перед каждым тестом. Инициализирует mock-объекты.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Проверяет, является ли чат активным.
     * Устанавливает активный чат и проверяет его состояние.
     */
    @Test
    void testIsActiveChat() {
        long chatId = 12345L;
        chatService.startChat(chatId, 67890L);

        assertTrue(chatService.isActiveChat(chatId));
        assertFalse(chatService.isActiveChat(11111L));
    }

    /**
     * Проверяет корректность маршрутизации сообщения от одного пользователя к другому.
     * Устанавливает активный чат и отправляет сообщение от одного пользователя другому.
     */
    @Test
    void testRouteMessage() {
        long senderChatId = 12345L;
        long recipientChatId = 67890L;
        chatService.startChat(senderChatId, recipientChatId);

        Message message = mock(Message.class);
        when(message.text()).thenReturn("Hello");

        chatService.routeMessage(senderChatId, message);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(recipientChatId, (long) actualMessage.getParameters().get("chat_id"));
        assertEquals("Hello", actualMessage.getParameters().get("text"));
    }

    /**
     * Проверяет корректность начала чата между клиентом и волонтером.
     * Устанавливает активный чат и проверяет, что волонтер становится занят.
     */
    @Test
    void testStartChat() {
        long clientChatId = 12345L;
        long volunteerChatId = 67890L;

        VolunteerSession volunteerSession = new VolunteerSession(volunteerChatId);
        when(volunteerService.getVolunteerSessions()).thenReturn(Map.of(volunteerChatId, volunteerSession));

        chatService.startChat(clientChatId, volunteerChatId);

        assertTrue(chatService.isActiveChat(clientChatId));
        assertTrue(volunteerSession.isBusy());
    }

    /**
     * Проверяет корректность завершения чата, инициированного клиентом.
     * Завершает чат и проверяет, что волонтер становится доступным.
     */
    @Test
    void testEndChat_clientInitiated() {
        long clientChatId = 12345L;
        long volunteerChatId = 67890L;

        VolunteerSession volunteerSession = new VolunteerSession(volunteerChatId);
        when(volunteerService.getVolunteerSessions()).thenReturn(Map.of(volunteerChatId, volunteerSession));

        chatService.startChat(clientChatId, volunteerChatId);
        chatService.endChat(clientChatId);

        assertFalse(chatService.isActiveChat(clientChatId));
        assertFalse(volunteerSession.isBusy());

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(volunteerChatId, (long) actualMessage.getParameters().get("chat_id"));
        assertEquals("Вы покинули беседу.", actualMessage.getParameters().get("text"));
    }

    /**
     * Проверяет корректность завершения чата, инициированного волонтером.
     * Завершает чат и проверяет, что клиент уведомляется о завершении беседы.
     */
    @Test
    void testEndChat_volunteerInitiated() {
        long clientChatId = 12345L;
        long volunteerChatId = 67890L;

        VolunteerSession volunteerSession = new VolunteerSession(volunteerChatId);
        when(volunteerService.getVolunteerSessions()).thenReturn(Map.of(volunteerChatId, volunteerSession));

        chatService.startChat(clientChatId, volunteerChatId);
        chatService.endChat(volunteerChatId);

        assertFalse(chatService.isActiveChat(clientChatId));
        assertFalse(volunteerSession.isBusy());

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(clientChatId, (long) actualMessage.getParameters().get("chat_id"));
        assertEquals("Волонтер покинул беседу.", actualMessage.getParameters().get("text"));
    }

    /**
     * Проверяет получение ID чата клиента по ID волонтера.
     * Устанавливает активный чат и проверяет корректность возврата ID клиента.
     */
    @Test
    void testGetClientChatIdForVolunteer() {
        long clientChatId = 12345L;
        long volunteerChatId = 67890L;

        chatService.startChat(clientChatId, volunteerChatId);

        long result = chatService.getClientChatIdForVolunteer(volunteerChatId);

        assertEquals(clientChatId, result);
    }

    /**
     * Проверяет уведомление доступного волонтера о новом запросе от клиента.
     * Уведомляет волонтера и проверяет, что отправлено корректное сообщение.
     */
    @Test
    void testNotifyVolunteer_availableVolunteer() {
        long clientChatId = 12345L;
        long volunteerChatId = 67890L;

        ClientSession clientSession = new ClientSession(clientChatId);
        VolunteerSession volunteerSession = new VolunteerSession(volunteerChatId);
        volunteerSession.setActive(true);

        Map<Long, VolunteerSession> volunteerSessions = Map.of(volunteerChatId, volunteerSession);

        chatService.notifyVolunteer(clientSession, volunteerSessions);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(volunteerChatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().get("text").toString().contains("просит ответить на вопросы."));
    }

    /**
     * Проверяет уведомление клиента, если нет доступных волонтеров.
     * Отправляет сообщение клиенту о недоступности волонтеров.
     */
    @Test
    void testNotifyVolunteer_noAvailableVolunteer() {
        long clientChatId = 12345L;

        ClientSession clientSession = new ClientSession(clientChatId);
        VolunteerSession volunteerSession = new VolunteerSession(67890L);
        volunteerSession.setBusy(true);  // волонтер занят

        Map<Long, VolunteerSession> volunteerSessions = Map.of(67890L, volunteerSession);

        chatService.notifyVolunteer(clientSession, volunteerSessions);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(clientChatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().get("text").toString().contains("К сожалению, в настоящее время нет доступных волонтеров."));
    }
}