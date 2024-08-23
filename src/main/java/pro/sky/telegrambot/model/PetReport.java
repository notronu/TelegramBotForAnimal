package pro.sky.telegrambot.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class PetReport {

    private long id;
    private String animalsDiet;
    private String animalHealth;
    private String animalHabits;
    private LocalDateTime data;


    private User user;


    public PetReport() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getAnimalsDiet() {
        return animalsDiet;
    }

    public void setAnimalsDiet(String animalsDiet) {
        this.animalsDiet = animalsDiet;
    }

    public String getAnimalHealth() {
        return animalHealth;
    }

    public void setAnimalHealth(String animalHealth) {
        this.animalHealth = animalHealth;
    }

    public String getAnimalHabits() {
        return animalHabits;
    }

    public void setAnimalHabits(String animalHabits) {
        this.animalHabits = animalHabits;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetReport petReport = (PetReport) o;
        return id == petReport.id && Objects.equals(animalsDiet, petReport.animalsDiet) && Objects.equals(animalHealth, petReport.animalHealth) && Objects.equals(animalHabits, petReport.animalHabits) && Objects.equals(data, petReport.data) && Objects.equals(user, petReport.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalsDiet, animalHealth, animalHabits, data, user);
    }

    @Override
    public String toString() {
        return "PetReport{" +
                "id=" + id +
                ", animalsDiet='" + animalsDiet + '\'' +
                ", animalHealth='" + animalHealth + '\'' +
                ", animalHabits='" + animalHabits + '\'' +
                ", data=" + data +
                ", user=" + user +
                '}';
    }
}

