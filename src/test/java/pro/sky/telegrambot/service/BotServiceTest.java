package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для класса {@link BotService}.
 * <p>
 * Этот класс проверяет корректность работы методов сервиса, отправляющих различные меню и сообщения через TelegramBot.
 * Используются mock-объекты для подмены зависимостей {@link TelegramBot}, {@link ChatService}, {@link ClientService},
 * {@link VolunteerService} и {@link FileService}.
 */
class BotServiceTest {

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
     * Mock-объект для работы с клиентским сервисом.
     */
    @Mock
    private ClientService clientService;

    /**
     * Mock-объект для работы с волонтёрским сервисом.
     */
    @Mock
    private VolunteerService volunteerService;

    /**
     * Mock-объект для работы с файловым сервисом.
     */
    @Mock
    private FileService fileService;

    /**
     * Внедрение mock-объектов в тестируемый сервис.
     */
    @InjectMocks
    private BotService botService;

    /**
     * Настройка окружения перед каждым тестом. Инициализирует mock-объекты.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Проверяет корректность отправки главного меню.
     */
    @Test
    void testSendMainMenu() {
        long chatId = 12345L;

        botService.sendMainMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Выберите опцию:"));
    }

    /**
     * Проверяет корректность отправки меню выбора приюта.
     */
    @Test
    void testSendShelterChoiceMenu() {
        long chatId = 12345L;

        botService.sendShelterChoiceMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Выберите приют:"));
    }

    /**
     * Проверяет корректность отправки меню инструкций.
     */
    @Test
    void testSendInstructionMenu() {
        long chatId = 12345L;

        botService.sendInstructionMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Выберите инструкцию:"));
    }

    /**
     * Проверяет корректность отправки запроса на отчёт о питомце.
     */
    @Test
    void testSendPetReport() {
        long chatId = 12345L;

        botService.sendPetReport(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Отправьте отчет о питомце: ..."));
    }

    /**
     * Проверяет корректность отправки информации о приюте для кошек.
     */
    @Test
    void testSendCatShelterInfo() {
        long chatId = 12345L;

        botService.sendCatShelterInfo(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Информация о приюте для кошек: ..."));
    }

    /**
     * Проверяет корректность отправки информации о приюте для собак.
     */
    @Test
    void testSendDogShelterInfo() {
        long chatId = 12345L;

        botService.sendDogShelterInfo(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Информация о приюте для собак: ..."));
    }

    /**
     * Проверяет корректность отправки инструкции по знакомству с животным.
     */
    @Test
    void testSendMeetAnimalInstruction() {
        long chatId = 12345L;

        botService.sendMeetAnimalInstruction(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Инструкция по знакомству с животным:"));
    }

    /**
     * Проверяет корректность отправки инструкции по взятию животного.
     */
    @Test
    void testSendTakeAnimalInstruction() {
        long chatId = 12345L;

        botService.sendTakeAnimalInstruction(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Инструкция как взять животное:"));
    }

    /**
     * Проверяет корректность отправки инструкции по обустройству дома для животного.
     */
    @Test
    void testSendSetupHomeInstruction() {
        long chatId = 12345L;

        botService.sendSetupHomeInstruction(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Инструкция по обустройству дома:"));
    }

    /**
     * Проверяет корректность отправки рекомендаций по проверенным кинологам.
     */
    @Test
    void testSendCynologistRecommendations() {
        long chatId = 12345L;

        botService.sendCynologistRecommendations(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Рекомендации по проверенным кинологам: ..."));
    }

    /**
     * Проверяет корректность отправки файла.
     */
    @Test
    void testSendFile() {
        long chatId = 12345L;
        String filePath = "path/to/file";

        botService.sendFile(chatId, filePath);

        verify(fileService).sendFile(chatId, filePath);
    }

    /**
     * Проверяет корректность вызова волонтёра для чата.
     */
    @Test
    void testCallVolunteer() {
        long chatId = 12345L;

        botService.callVolunteer(chatId);

        verify(clientService).callVolunteer(chatId);
    }

    /**
     * Проверяет корректность присоединения к чату волонтёра с клиентом.
     */
    @Test
    void testJoinChat() {
        long chatId = 12345L;
        long clientChatId = 67890L;

        when(chatService.getClientChatIdForVolunteer(chatId)).thenReturn(clientChatId);

        botService.joinChat(chatId);

        verify(chatService).startChat(clientChatId, chatId);
        verify(telegramBot, times(2)).execute(any(SendMessage.class)); // одно сообщение для клиента, другое для волонтера
    }

    /**
     * Проверяет корректность выхода из чата.
     */
    @Test
    void testLeaveChat() {
        long chatId = 12345L;

        botService.leaveChat(chatId);

        verify(chatService).endChat(chatId);
    }

    /**
     * Проверяет корректность регистрации волонтёра.
     */
    @Test
    void testRegisterVolunteer() {
        long chatId = 12345L;

        botService.registerVolunteer(chatId);

        verify(volunteerService).registerVolunteer(chatId);
        verify(telegramBot).execute(any(SendMessage.class));
    }

    /**
     * Проверяет корректность установки статуса активности волонтёра.
     */
    @Test
    void testSetVolunteerActive() {
        long chatId = 12345L;
        boolean active = true;

        botService.setVolunteerActive(chatId, active);

        verify(volunteerService).setVolunteerActive(chatId, active);
    }

    /**
     * Проверяет корректность отправки меню волонтёра.
     */
    @Test
    void testSendVolunteerMenu() {
        long chatId = 12345L;

        botService.sendVolunteerMenu(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());

        SendMessage actualMessage = argumentCaptor.getValue();
        assertEquals(chatId, (long) actualMessage.getParameters().get("chat_id"));
        assertTrue(actualMessage.getParameters().containsValue("Выберите статус:"));
    }

    /**
     * Проверяет корректность завершения чата волонтёра с клиентом.
     */
    @Test
    void testEndChat() {
        long chatId = 12345L;
        long clientChatId = 67890L;

        when(chatService.getClientChatIdForVolunteer(chatId)).thenReturn(clientChatId);

        botService.endChat(chatId);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(4)).execute(argumentCaptor.capture());

        assertEquals(clientChatId, (long) argumentCaptor.getAllValues().get(0).getParameters().get("chat_id"));
        assertEquals("Волонтер прекратил беседу с вами.", argumentCaptor.getAllValues().get(0).getParameters().get("text"));

        assertEquals(chatId, (long) argumentCaptor.getAllValues().get(1).getParameters().get("chat_id"));
        assertEquals("Вы прекратили беседу с клиентом.", argumentCaptor.getAllValues().get(1).getParameters().get("text"));

        assertEquals(clientChatId, (long) argumentCaptor.getAllValues().get(2).getParameters().get("chat_id"));
        assertEquals("Выберите опцию:", argumentCaptor.getAllValues().get(2).getParameters().get("text"));

        assertEquals(chatId, (long) argumentCaptor.getAllValues().get(3).getParameters().get("chat_id"));
        assertEquals("Выберите статус:", argumentCaptor.getAllValues().get(3).getParameters().get("text"));
    }

    /**
     * Проверяет обработку обновления с командой, отправленной в Telegram-бот.
     */
    @Test
    void testHandleUpdate_withCommand() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/start");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);

        botService.handleUpdate(update);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    /**
     * Проверяет обработку обновления, если чат активен.
     */
    @Test
    void testHandleUpdate_withActiveChat() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("Some text");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);
        when(chatService.isActiveChat(12345L)).thenReturn(true);

        botService.handleUpdate(update);

        verify(chatService).routeMessage(12345L, message);
    }

    /**
     * Проверяет обработку обновления с неизвестной командой.
     */
    @Test
    void testHandleUpdate_withDefaultCommand() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("Unknown command");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);
        when(chatService.isActiveChat(12345L)).thenReturn(false);

        botService.handleUpdate(update);

        verify(telegramBot).execute(any(SendMessage.class));
    }
}
