package pro.sky.telegrambot.model;

/**
 * Класс, представляющий сессию чата между клиентом и волонтером.
 */
public class ChatSession {
    private final long clientChatId;
    private final long volunteerChatId;
    private boolean active;

    /**
     * Создает новую сессию чата.
     * @param clientChatId идентификатор чата клиента
     * @param volunteerChatId идентификатор чата волонтера
     */
    public ChatSession(long clientChatId, long volunteerChatId) {
        this.clientChatId = clientChatId;
        this.volunteerChatId = volunteerChatId;
        this.active = true;
    }

    public long getClientChatId() {
        return clientChatId;
    }

    public long getVolunteerChatId() {
        return volunteerChatId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}