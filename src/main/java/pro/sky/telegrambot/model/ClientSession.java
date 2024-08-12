package pro.sky.telegrambot.model;

public class ClientSession {
    private final long chatId;
    private String clientName;
    private String clientQuestion;

    public ClientSession(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientQuestion() {
        return clientQuestion;
    }

    public void setClientQuestion(String clientQuestion) {
        this.clientQuestion = clientQuestion;
    }
}