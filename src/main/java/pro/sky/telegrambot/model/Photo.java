package pro.sky.telegrambot.model;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "photo")

public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID записи в базе данных
    @Column(name = "sent_time")
    public Date sentTime;
    private String fileId;
    private Long chatId;
    private String login;
    @Column(name = "text")
    private String text;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }



    public void execute(SendMessage sendMessage) {
    }
}
