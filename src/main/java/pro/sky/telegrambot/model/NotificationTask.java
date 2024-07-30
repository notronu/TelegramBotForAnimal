package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long chat_id;
    private String text_msg;

    @Column(name = "phone")
    private String phone;

    public NotificationTask(long chat_id, String text_msg, String phone) {
        this.chat_id = chat_id;
        this.text_msg = text_msg;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getChat_id() {
        return chat_id;
    }
    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }
    public String getText_msg() {
        return text_msg;
    }
    public void setText_msg(String text_msg) {
        this.text_msg = text_msg;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                NotificationTask that = (NotificationTask) o;
                return chat_id == that.chat_id && Objects.equals(id, that.id) && Objects.equals(text_msg, that.text_msg) && Objects.equals(phone, that.phone);
            }

            @Override
            public int hashCode() {
                return Objects.hash(id, chat_id, text_msg, phone);
            }

            @Override
            public String toString() {
                return "NotificationTask{" +
                        "id=" + id +
                        ", chat_id=" + chat_id +
                        ", text_msg='" + text_msg + '\'' +
                        ", phone=" + phone +
                        '}';
            }
        }

