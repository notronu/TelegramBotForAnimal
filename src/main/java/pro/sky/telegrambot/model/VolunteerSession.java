package pro.sky.telegrambot.model;

/**
 * Класс, представляющий сессию волонтера.
 */
public class VolunteerSession {
    private final long chatId;
    private boolean busy;
    private boolean active;

    /**
     * Создает новую сессию волонтера с указанным идентификатором чата.
     * @param chatId идентификатор чата
     */
    public VolunteerSession(long chatId) {
        this.chatId = chatId;
        this.busy = false;
        this.active = false;
    }

    public long getChatId() {
        return chatId;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}