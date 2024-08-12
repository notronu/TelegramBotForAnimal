package pro.sky.telegrambot.model;

public class VolunteerSession {
    private final long chatId;
    private boolean busy;
    private boolean active;

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