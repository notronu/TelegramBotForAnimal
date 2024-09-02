package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "chat_Id")
    private long chatId;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "login")
    private String login;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Pet pet;

    public User(long chatId, String name, String phone, String login) {
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
        this.login = login;
    }

    public User() {

    }

    public long getId() {
        return userId;
    }

    public void setId(long id) {
        this.userId = userId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && chatId == user.chatId && Objects.equals(name, user.name) && Objects.equals(phone, user.phone) && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId, name, phone, login);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
